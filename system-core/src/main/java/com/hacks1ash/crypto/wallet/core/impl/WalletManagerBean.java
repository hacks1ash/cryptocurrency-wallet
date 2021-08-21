package com.hacks1ash.crypto.wallet.core.impl;

import com.hacks1ash.crypto.wallet.blockchain.GenericRpcException;
import com.hacks1ash.crypto.wallet.blockchain.UTXORPCClient;
import com.hacks1ash.crypto.wallet.blockchain.bitcoin.model.request.CreateWalletRequest;
import com.hacks1ash.crypto.wallet.blockchain.bitcoin.model.request.GetBalanceRequest;
import com.hacks1ash.crypto.wallet.blockchain.bitcoin.model.request.ImportPrivateKeyRequest;
import com.hacks1ash.crypto.wallet.core.WalletException;
import com.hacks1ash.crypto.wallet.core.WalletManager;
import com.hacks1ash.crypto.wallet.core.model.Address;
import com.hacks1ash.crypto.wallet.core.model.CryptoCurrency;
import com.hacks1ash.crypto.wallet.core.model.request.AddressCreationRequest;
import com.hacks1ash.crypto.wallet.core.model.request.TransactionRequest;
import com.hacks1ash.crypto.wallet.core.model.request.WalletCreationRequest;
import com.hacks1ash.crypto.wallet.core.model.response.AddressResponse;
import com.hacks1ash.crypto.wallet.core.model.response.EstimateFeeResponse;
import com.hacks1ash.crypto.wallet.core.model.response.SendTransactionResponse;
import com.hacks1ash.crypto.wallet.core.model.response.WalletResponse;
import com.hacks1ash.crypto.wallet.core.storage.WalletRepository;
import com.hacks1ash.crypto.wallet.core.storage.document.Wallet;
import com.hacks1ash.crypto.wallet.core.utils.BlockchainIntegrationFactory;
import com.hacks1ash.crypto.wallet.core.utils.CurrencyUtils;
import com.hacks1ash.crypto.wallet.core.utils.MnemonicWords;
import org.bitcoinj.core.ECKey;
import org.bitcoinj.core.LegacyAddress;
import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.core.SegwitAddress;
import org.bitcoinj.crypto.DeterministicKey;
import org.bitcoinj.crypto.HDKeyDerivation;
import org.bitcoinj.script.Script;
import org.bitcoinj.script.ScriptBuilder;
import org.bitcoinj.script.ScriptPattern;
import org.bitcoinj.wallet.DeterministicSeed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class WalletManagerBean implements WalletManager {

  private WalletRepository walletRepository;

  private MnemonicWords mnemonicWords;

  private BlockchainIntegrationFactory blockchainFactory;

  @Override
  public WalletResponse createWallet(WalletCreationRequest request) {
    synchronized (request.getName()) {
      CryptoCurrency cryptoCurrency = CryptoCurrency.cryptoCurrencyFromShortName(request.getCurrency());
      UTXORPCClient rpcClient = blockchainFactory.getRPCClient(cryptoCurrency);

      List<String> hdSeed;
      if (request.getHdSeed() == null) {
        hdSeed = mnemonicWords.getRandomWords(12);
      } else {
        hdSeed = Arrays.stream(request.getHdSeed().split(" ")).collect(Collectors.toList());
        if (hdSeed.size() != 12) {
          throw new WalletException.InvalidHDSeed(hdSeed.size());
        }
      }
      long creationTimestamp = Instant.now().getEpochSecond();
      new DeterministicSeed(hdSeed, null, "", creationTimestamp);

      String nodeWalletName = UUID.randomUUID().toString();

      try {
        rpcClient.createWallet(new CreateWalletRequest(nodeWalletName));
      } catch (GenericRpcException ex) {
        throw new WalletException(ex.getErrorKey(), ex.getErrorMessage(), ex.getErrorCode());
      }

      Wallet wallet = walletRepository.save(
        new Wallet(
          request.getName(),
          nodeWalletName,
          cryptoCurrency,
          hdSeed,
          creationTimestamp,
          new ArrayList<>()
        )
      );

      return new WalletResponse(wallet.getId(), wallet.getName(), wallet.getCurrency(), String.join(" ", wallet.getHdSeed()));
    }
  }

  @Override
  public List<WalletResponse> listWallets() {
    List<Wallet> wallets = walletRepository.findAll();
    return wallets
      .stream()
      .map(w -> new WalletResponse(w.getId(), w.getName(), w.getCurrency(), String.join(" ", w.getHdSeed())))
      .collect(Collectors.toList());
  }

  @Override
  public BigInteger getBalance(String walletId) {
    Optional<Wallet> optionalWallet = walletRepository.findById(walletId);
    if (optionalWallet.isPresent()) {
      Wallet wallet = optionalWallet.get();
      UTXORPCClient rpcClient = blockchainFactory.getRPCClient(wallet.getCurrency());
      synchronized (wallet) {
        BigDecimal resp = rpcClient.getBalance(new GetBalanceRequest(wallet.getNodeWalletNameAlias()));
        return CurrencyUtils.toMinorUnit(wallet.getCurrency(), resp);
      }
    }
    throw new WalletException.WalletNotFound(walletId);
  }

  @Override
  public AddressResponse createAddress(String walletId, AddressCreationRequest request) {
    Optional<Wallet> optionalWallet = walletRepository.findById(walletId);
    if (optionalWallet.isPresent()) {
      Wallet wallet = optionalWallet.get();
      synchronized (wallet) {
        NetworkParameters networkParameters = wallet.getCurrency().getNetworkParameters();
        UTXORPCClient rpcClient = blockchainFactory.getRPCClient(wallet.getCurrency());
        int addressIndex = wallet.getAddresses().size();
        DeterministicSeed masterSeed = new DeterministicSeed(wallet.getHdSeed(), null, "", wallet.getCreationTimestamp());
        byte[] seed = Objects.requireNonNull(masterSeed.getSeedBytes());
        DeterministicKey masterPrivateKey = HDKeyDerivation.createMasterPrivateKey(seed);

        DeterministicKey deterministicKey = HDKeyDerivation.deriveChildKey(masterPrivateKey, addressIndex);
        ECKey ecKey = ECKey.fromPrivate(deterministicKey.getPrivKey());

        String address;
        switch (request.getAddressType()) {
          case P2PKH:
            address = LegacyAddress.fromKey(networkParameters, ecKey).toString();
            break;
          case BECH_32:
            address = SegwitAddress.fromKey(networkParameters, ecKey, Script.ScriptType.P2WPKH).toString();
            break;
          case DEFAULT:
          case P2SH:
          default:
            Script redeemScript = ScriptBuilder.createP2WPKHOutputScript(ecKey);
            Script script = ScriptBuilder.createP2SHOutputScript(redeemScript);
            byte[] scriptHash = ScriptPattern.extractHashFromP2SH(script);
            address = LegacyAddress.fromScriptHash(networkParameters, scriptHash).toString();
            break;
        }

        try {
          rpcClient.importPrivateKey(
            new ImportPrivateKeyRequest(
              wallet.getNodeWalletNameAlias(),
              deterministicKey.getPrivateKeyAsWiF(networkParameters),
              request.getName(),
              false
            )
          );
        } catch (GenericRpcException ex) {
          throw new WalletException(ex.getErrorKey(), ex.getErrorMessage(), ex.getErrorCode());
        }

        wallet.getAddresses().add(new Address(request.getName(), address, request.getAddressType(), addressIndex));
        walletRepository.save(wallet);
        return new AddressResponse(wallet.getId(), request.getName(), address, request.getAddressType());
      }
    }
    throw new WalletException.WalletNotFound(walletId);
  }

  @Override
  public List<AddressResponse> getAddresses(String walletId) {
    Optional<Wallet> optionalWallet = walletRepository.findById(walletId);
    if (optionalWallet.isPresent()) {
      Wallet wallet = optionalWallet.get();
      return wallet.getAddresses()
        .stream()
        .map(a -> new AddressResponse(walletId, a.getName(), a.getAddress(), a.getType()))
        .collect(Collectors.toList());
    }
    throw new WalletException.WalletNotFound(walletId);
  }

  @Override
  public EstimateFeeResponse estimateFee(String walletId, TransactionRequest request) {
    return null;
  }

  @Override
  public SendTransactionResponse sendTransaction(String walletId, TransactionRequest request) {
    return null;
  }

  @Autowired
  public void setWalletRepository(WalletRepository walletRepository) {
    this.walletRepository = walletRepository;
  }

  @Autowired
  public void setMnemonicWords(MnemonicWords mnemonicWords) {
    this.mnemonicWords = mnemonicWords;
  }

  @Autowired
  public void setBlockchainFactory(BlockchainIntegrationFactory blockchainFactory) {
    this.blockchainFactory = blockchainFactory;
  }
}

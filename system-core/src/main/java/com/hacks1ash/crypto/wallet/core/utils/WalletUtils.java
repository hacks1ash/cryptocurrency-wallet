package com.hacks1ash.crypto.wallet.core.utils;

import com.hacks1ash.crypto.wallet.blockchain.GenericRpcException;
import com.hacks1ash.crypto.wallet.blockchain.UTXORPCClient;
import com.hacks1ash.crypto.wallet.blockchain.bitcoin.model.BitcoinRawTxBuilder;
import com.hacks1ash.crypto.wallet.blockchain.bitcoin.model.request.EstimateSmartFeeRequest;
import com.hacks1ash.crypto.wallet.blockchain.bitcoin.model.request.FundRawTransactionRequest;
import com.hacks1ash.crypto.wallet.blockchain.bitcoin.model.request.ImportPrivateKeyRequest;
import com.hacks1ash.crypto.wallet.blockchain.bitcoin.model.response.FundRawTransactionResponse;
import com.hacks1ash.crypto.wallet.core.WalletException;
import com.hacks1ash.crypto.wallet.core.model.AddressType;
import com.hacks1ash.crypto.wallet.core.model.CryptoCurrency;
import com.hacks1ash.crypto.wallet.core.model.TransactionRecipient;
import com.hacks1ash.crypto.wallet.core.model.request.TransactionRequest;
import com.hacks1ash.crypto.wallet.core.storage.document.Wallet;
import org.bitcoinj.core.ECKey;
import org.bitcoinj.core.LegacyAddress;
import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.core.SegwitAddress;
import org.bitcoinj.crypto.DeterministicKey;
import org.bitcoinj.crypto.HDKeyDerivation;
import org.bitcoinj.script.Script;
import org.bitcoinj.script.ScriptBuilder;
import org.bitcoinj.script.ScriptPattern;

import java.util.Collections;

public class WalletUtils {

  public static FundRawTransactionResponse fundRawTransaction(TransactionRequest request, Wallet wallet, CryptoCurrency currency, UTXORPCClient rpcClient) {
    BitcoinRawTxBuilder bitcoinRawTxBuilder = new BitcoinRawTxBuilder(rpcClient, wallet.getNodeWalletNameAlias());
    for (TransactionRecipient recipient : request.getRecipients()) {
      bitcoinRawTxBuilder = bitcoinRawTxBuilder.out(recipient.getAddress(), CurrencyUtils.toMajorUnit(currency, recipient.getAmount()));
    }
    String txId = bitcoinRawTxBuilder.create();
    return rpcClient.fundRawTransaction(
      new FundRawTransactionRequest(
        wallet.getNodeWalletNameAlias(),
        txId,
        true,
        wallet.getChangeAddress(),
        null,
        null,
        false,
        false,
        request.getFeePerSatoshi(),
        null,
        Collections.emptyList(),
        false,
        request.getSpeed() == null ? null : request.getSpeed().getBlockSize(),
        request.getSpeed() == null ? null : EstimateSmartFeeRequest.EstimateMode.CONSERVATIVE,
        false
      )
    );
  }

  public static String createAddress(UTXORPCClient rpcClient, String walletId, DeterministicKey masterPrivateKey, String addressName, AddressType addressType, NetworkParameters networkParameters, int addressIndex) {
    DeterministicKey deterministicKey = HDKeyDerivation.deriveChildKey(masterPrivateKey, addressIndex);
    ECKey ecKey = ECKey.fromPrivate(deterministicKey.getPrivKey());

    String address;
    switch (addressType) {
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
          walletId,
          deterministicKey.getPrivateKeyAsWiF(networkParameters),
          addressName,
          false
        )
      );
    } catch (GenericRpcException ex) {
      throw new WalletException(ex.getErrorKey(), ex.getErrorMessage(), ex.getErrorCode());
    }

    return address;
  }

}

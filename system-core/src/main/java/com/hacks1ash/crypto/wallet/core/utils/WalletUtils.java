package com.hacks1ash.crypto.wallet.core.utils;

import com.hacks1ash.crypto.wallet.blockchain.GenericRpcException;
import com.hacks1ash.crypto.wallet.blockchain.UTXORPCClient;
import com.hacks1ash.crypto.wallet.blockchain.bitcoin.model.BitcoinRawTxBuilder;
import com.hacks1ash.crypto.wallet.blockchain.bitcoin.model.request.EstimateSmartFeeRequest;
import com.hacks1ash.crypto.wallet.blockchain.bitcoin.model.request.FundRawTransactionRequest;
import com.hacks1ash.crypto.wallet.blockchain.bitcoin.model.request.ImportMultiRequest;
import com.hacks1ash.crypto.wallet.blockchain.bitcoin.model.request.ImportPrivateKeyRequest;
import com.hacks1ash.crypto.wallet.blockchain.bitcoin.model.response.FundRawTransactionResponse;
import com.hacks1ash.crypto.wallet.core.WalletException;
import com.hacks1ash.crypto.wallet.core.model.AddressType;
import com.hacks1ash.crypto.wallet.core.model.CryptoCurrency;
import com.hacks1ash.crypto.wallet.core.model.TransactionRecipient;
import com.hacks1ash.crypto.wallet.core.model.request.TransactionRecipientRequest;
import com.hacks1ash.crypto.wallet.core.model.request.TransactionRequest;
import com.hacks1ash.crypto.wallet.core.model.response.GetTransactionResponse;
import com.hacks1ash.crypto.wallet.core.storage.document.Wallet;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bitcoinj.core.ECKey;
import org.bitcoinj.core.LegacyAddress;
import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.core.SegwitAddress;
import org.bitcoinj.crypto.DeterministicKey;
import org.bitcoinj.crypto.HDKeyDerivation;
import org.bitcoinj.script.Script;
import org.bitcoinj.script.ScriptBuilder;
import org.bitcoinj.script.ScriptPattern;

import java.util.*;
import java.util.stream.Collectors;

public class WalletUtils {

  public static List<GetTransactionResponse> formatTransactions(List<GetTransactionResponse> dataToFormat) {
    Map<String, GetTransactionResponse> dataMap = new HashMap<>();
    for (GetTransactionResponse transaction : dataToFormat) {
      GetTransactionResponse data = dataMap.get(transaction.getTxId());
      if (data != null) {
        List<TransactionRecipient> participants = new ArrayList<>(data.getParticipants());
        participants.addAll(transaction.getParticipants());
        data.setParticipants(participants);
      } else {
        dataMap.put(transaction.getTxId(), transaction);
      }
    }
    return new ArrayList<>(dataMap.values());
  }

  public static FundRawTransactionResponse fundRawTransaction(TransactionRequest request, Wallet wallet, CryptoCurrency currency, UTXORPCClient rpcClient) {
    BitcoinRawTxBuilder bitcoinRawTxBuilder = new BitcoinRawTxBuilder(rpcClient, wallet.getNodeWalletNameAlias());
    List<Integer> subtractFeeFromOutputs = new ArrayList<>();
    for (int i = 0; i < request.getRecipients().size(); i++) {
      TransactionRecipientRequest recipient = request.getRecipients().get(i);
      bitcoinRawTxBuilder = bitcoinRawTxBuilder.out(recipient.getAddress(), CurrencyUtils.toMajorUnit(currency, recipient.getAmount()));
      if (recipient.isSubtractFee()) {
        subtractFeeFromOutputs.add(i);
      }
    }
    String txId = bitcoinRawTxBuilder.create();
    return rpcClient.fundRawTransaction(
      new FundRawTransactionRequest(
        wallet.getNodeWalletNameAlias(),
        txId,
        true,
        wallet.getChangeAddress(),
        0,
        null,
        false,
        false,
        request.getFeePerByte(),
        null,
        subtractFeeFromOutputs,
        false,
        request.getSpeed() == null ? null : request.getSpeed().getBlockSize(),
        request.getSpeed() == null ? null : EstimateSmartFeeRequest.EstimateMode.CONSERVATIVE,
        false
      )
    );
  }

  public static String createChangeAddress(UTXORPCClient rpcClient, String walletId, DeterministicKey masterPrivateKey, NetworkParameters networkParameters, int addressIndex) {
    AddressWithPrivate address = createAddress(masterPrivateKey, AddressType.BECH_32, networkParameters, addressIndex);
    try {
      rpcClient.importMulti(
        walletId,
        Collections.singletonList(
          new ImportMultiRequest(
            new HashMap<String, String>() {{
              put("address", address.getAddress());
            }},
            null,
            null,
            Collections.singletonList(address.getPrivateKey()),
            true,
            null
          )
        ),
        false
      );
    } catch (GenericRpcException ex) {
      throw new WalletException(ex.getErrorKey(), ex.getErrorMessage(), ex.getErrorCode());
    }
    return address.getAddress();
  }

  public static String createAddress(UTXORPCClient rpcClient, String walletId, DeterministicKey masterPrivateKey, String addressName, AddressType addressType, NetworkParameters networkParameters, int addressIndex) {
    AddressWithPrivate address = createAddress(masterPrivateKey, addressType, networkParameters, addressIndex);

    try {
      rpcClient.importPrivateKey(
        new ImportPrivateKeyRequest(
          walletId,
          address.getPrivateKey(),
          addressName,
          false
        )
      );
    } catch (GenericRpcException ex) {
      throw new WalletException(ex.getErrorKey(), ex.getErrorMessage(), ex.getErrorCode());
    }

    return address.getAddress();
  }

  private static AddressWithPrivate createAddress(DeterministicKey masterPrivateKey, AddressType addressType, NetworkParameters networkParameters, int addressIndex) {
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

    return new AddressWithPrivate(address, deterministicKey.getPrivateKeyAsWiF(networkParameters));
  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  private static class AddressWithPrivate {
    private String address;
    private String privateKey;
  }

}

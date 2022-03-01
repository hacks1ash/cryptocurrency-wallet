package com.hacks1ash.crypto.wallet.core.utils;

import com.hacks1ash.crypto.wallet.blockchain.UTXORPCClient;
import com.hacks1ash.crypto.wallet.blockchain.model.UTXORawTxBuilder;
import com.hacks1ash.crypto.wallet.blockchain.model.request.EstimateSmartFeeRequest;
import com.hacks1ash.crypto.wallet.blockchain.model.request.FundRawTransactionRequest;
import com.hacks1ash.crypto.wallet.blockchain.model.response.FundRawTransactionResponse;
import com.hacks1ash.crypto.wallet.core.model.CryptoCurrency;
import com.hacks1ash.crypto.wallet.core.model.TransactionRecipient;
import com.hacks1ash.crypto.wallet.core.model.request.TransactionRecipientRequest;
import com.hacks1ash.crypto.wallet.core.model.request.TransactionRequest;
import com.hacks1ash.crypto.wallet.core.model.response.GetTransactionResponse;
import com.hacks1ash.crypto.wallet.core.storage.document.Wallet;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    UTXORawTxBuilder bitcoinRawTxBuilder = new UTXORawTxBuilder(rpcClient, wallet.getNodeWalletNameAlias());
    List<Integer> subtractFeeFromOutputs = new ArrayList<>();
    for (int i = 0; i < request.getRecipients().size(); i++) {
      TransactionRecipientRequest recipient = request.getRecipients().get(i);
      bitcoinRawTxBuilder = bitcoinRawTxBuilder.out(recipient.getAddress(), CurrencyUtils.toMajorUnit(currency, recipient.getAmount()));
      if (recipient.isSubtractFee()) {
        subtractFeeFromOutputs.add(i);
      }
    }
    String txId = bitcoinRawTxBuilder.create(currency.getUtxoProvider(), currency.getNetworkParams());
    return rpcClient.fundRawTransaction(
      new FundRawTransactionRequest(
        currency.getUtxoProvider(),
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
      ),
      currency.getNetworkParams()
    );
  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  private static class AddressWithPrivate {
    private String address;
    private String privateKey;
  }

}

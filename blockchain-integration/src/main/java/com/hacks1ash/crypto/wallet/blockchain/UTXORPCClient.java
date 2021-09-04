package com.hacks1ash.crypto.wallet.blockchain;

import com.hacks1ash.crypto.wallet.blockchain.bitcoin.model.BitcoinRawTxBuilder;
import com.hacks1ash.crypto.wallet.blockchain.bitcoin.model.request.*;
import com.hacks1ash.crypto.wallet.blockchain.bitcoin.model.response.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public interface UTXORPCClient {

  CreateWalletResponse createWallet(CreateWalletRequest request);

  BigDecimal getBalance(GetBalanceRequest request);

  EstimateSmartFeeResponse estimateSmartFee(EstimateSmartFeeRequest request);

  void importPrivateKey(ImportPrivateKeyRequest request);

  String createRawTransaction(String walletId, ArrayList<BitcoinRawTxBuilder.TxInput> txInputs, List<BitcoinRawTxBuilder.TxOutput> outputs);

  FundRawTransactionResponse fundRawTransaction(FundRawTransactionRequest request);

  SignRawTransactionWithWalletResponse singRawTransactionWithWallet(String walletId, String txHex);

  String sendRawTransaction(String txHex);

  GetTrasactionResponse getTransaction(GetTransactionRequest request);

  void importMulti(String walletId, List<ImportMultiRequest> addresses, boolean rescan);

  List<ListTransactionResponse> listTransactions(ListTransactionRequest request);
}

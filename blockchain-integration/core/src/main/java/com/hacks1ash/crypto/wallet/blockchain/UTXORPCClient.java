package com.hacks1ash.crypto.wallet.blockchain;

import com.hacks1ash.crypto.wallet.blockchain.bitcoin.model.BitcoinRawTxBuilder;
import com.hacks1ash.crypto.wallet.blockchain.bitcoin.model.request.*;
import com.hacks1ash.crypto.wallet.blockchain.bitcoin.model.response.*;
import com.hacks1ash.crypto.wallet.blockchain.factory.UTXOProvider;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public interface UTXORPCClient {

  CreateWalletResponse createWallet(CreateWalletRequest request);

  BigDecimal getBalance(GetBalanceRequest request);

  EstimateSmartFeeResponse estimateSmartFee(EstimateSmartFeeRequest request);

  void importPrivateKey(ImportPrivateKeyRequest request);

  String createRawTransaction(UTXOProvider utxoProvider, String walletId, ArrayList<BitcoinRawTxBuilder.TxInput> txInputs, List<BitcoinRawTxBuilder.TxOutput> outputs);

  FundRawTransactionResponse fundRawTransaction(FundRawTransactionRequest request);

  SignRawTransactionWithWalletResponse singRawTransactionWithWallet(UTXOProvider utxoProvider, String walletId, String txHex);

  String sendRawTransaction(UTXOProvider utxoProvider, String txHex);

  GetTrasactionResponse getTransaction(GetTransactionRequest request);

  void importMulti(UTXOProvider utxoProvider, String walletId, List<ImportMultiRequest> addresses, boolean rescan);

  List<ListTransactionResponse> listTransactions(ListTransactionRequest request);
}

package com.hacks1ash.crypto.wallet.blockchain;

import com.hacks1ash.crypto.wallet.blockchain.factory.UTXOProvider;
import com.hacks1ash.crypto.wallet.blockchain.model.NetworkParams;
import com.hacks1ash.crypto.wallet.blockchain.model.UTXORawTxBuilder;
import com.hacks1ash.crypto.wallet.blockchain.model.request.*;
import com.hacks1ash.crypto.wallet.blockchain.model.response.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public interface UTXORPCClient {

  CreateWalletResponse createWallet(CreateWalletRequest request, NetworkParams networkParams);

  BigDecimal getBalance(GetBalanceRequest request, NetworkParams networkParams);

  EstimateSmartFeeResponse estimateSmartFee(EstimateSmartFeeRequest request, NetworkParams networkParams);

  void importPrivateKey(ImportPrivateKeyRequest request, NetworkParams networkParams);

  String createRawTransaction(UTXOProvider utxoProvider, String walletId, ArrayList<UTXORawTxBuilder.TxInput> txInputs, List<UTXORawTxBuilder.TxOutput> outputs, NetworkParams networkParams);

  FundRawTransactionResponse fundRawTransaction(FundRawTransactionRequest request, NetworkParams networkParams);

  SignRawTransactionWithWalletResponse singRawTransactionWithWallet(UTXOProvider utxoProvider, String walletId, String txHex, NetworkParams networkParams);

  String sendRawTransaction(UTXOProvider utxoProvider, String txHex, NetworkParams networkParams);

  GetTrasactionResponse getTransaction(GetTransactionRequest request, NetworkParams networkParams);

  void importMulti(UTXOProvider utxoProvider, String walletId, List<ImportMultiRequest> addresses, boolean rescan, NetworkParams networkParams);

  List<ListTransactionResponse> listTransactions(ListTransactionRequest request, NetworkParams networkParams);
}

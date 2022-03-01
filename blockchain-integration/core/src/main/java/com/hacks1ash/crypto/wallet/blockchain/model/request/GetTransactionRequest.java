package com.hacks1ash.crypto.wallet.blockchain.model.request;

import com.hacks1ash.crypto.wallet.blockchain.factory.UTXOProvider;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetTransactionRequest {

  private UTXOProvider utxoProvider;

  private String walletId;

  private String txId;

  private boolean includeWatchOnly = true;

  private boolean verbose = false;

  public GetTransactionRequest(UTXOProvider utxoProvider, String walletId, String txId) {
    this.utxoProvider = utxoProvider;
    this.walletId = walletId;
    this.txId = txId;
  }

}

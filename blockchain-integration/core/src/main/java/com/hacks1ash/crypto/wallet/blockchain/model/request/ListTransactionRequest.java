package com.hacks1ash.crypto.wallet.blockchain.model.request;

import com.hacks1ash.crypto.wallet.blockchain.factory.UTXOProvider;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ListTransactionRequest {

  private UTXOProvider utxoProvider;

  private String walletId;

  private String label = "*";

  private int count = 10;

  private int skip = 0;

  private boolean includeWatchOnly = true;

  public ListTransactionRequest(UTXOProvider utxoProvider, String walletId, int count, int skip) {
    this.utxoProvider = utxoProvider;
    this.walletId = walletId;
    this.count = count;
    this.skip = skip;
  }

  public ListTransactionRequest(UTXOProvider utxoProvider, String walletId, int count) {
    this(utxoProvider, walletId, count, 0);
  }

}

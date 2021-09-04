package com.hacks1ash.crypto.wallet.blockchain.bitcoin.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ListTransactionRequest {

  private String walletId;

  private String label = "*";

  private int count = 10;

  private int skip = 0;

  private boolean includeWatchOnly = true;

  public ListTransactionRequest(String walletId, int count, int skip) {
    this.walletId = walletId;
    this.count = count;
    this.skip = skip;
  }

  public ListTransactionRequest(String walletId, int count) {
    this(walletId, count, 0);
  }

}

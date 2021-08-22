package com.hacks1ash.crypto.wallet.blockchain.bitcoin.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetTransactionRequest {

  private String walletId;

  private String txId;

  private boolean includeWatchOnly = true;

  private boolean verbose = false;

  public GetTransactionRequest(String walletId, String txId) {
    this.walletId = walletId;
    this.txId = txId;
  }

}

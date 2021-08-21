package com.hacks1ash.crypto.wallet.blockchain.bitcoin.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetBalanceRequest {

  private String walletId;

  private int minConf = 0;

  private boolean includeWatchonly = true;

  private boolean avoidReuse = false;

  public GetBalanceRequest(String walletId) {
    this.walletId = walletId;
  }

}

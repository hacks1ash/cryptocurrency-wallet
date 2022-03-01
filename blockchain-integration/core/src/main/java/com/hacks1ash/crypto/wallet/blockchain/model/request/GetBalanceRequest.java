package com.hacks1ash.crypto.wallet.blockchain.model.request;

import com.hacks1ash.crypto.wallet.blockchain.factory.UTXOProvider;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetBalanceRequest {

  private UTXOProvider utxoProvider;

  private String walletId;

  private int minConf = 0;

  private boolean includeWatchonly = true;

  private boolean avoidReuse = false;

  public GetBalanceRequest(UTXOProvider utxoProvider, String walletId) {
    this.utxoProvider = utxoProvider;
    this.walletId = walletId;
  }

}

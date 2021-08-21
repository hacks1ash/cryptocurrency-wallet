package com.hacks1ash.crypto.wallet.blockchain.bitcoin.model.request;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ImportPrivateKeyRequest {

  private String walletId;

  private String privateKey;

  private String label = "";

  private boolean rescan = true;

  public ImportPrivateKeyRequest(String walletId, String privateKey) {
    this.walletId = walletId;
    this.privateKey = privateKey;
  }

}

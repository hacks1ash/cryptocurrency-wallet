package com.hacks1ash.crypto.wallet.blockchain.bitcoin.model.request;

import com.hacks1ash.crypto.wallet.blockchain.factory.UTXOProvider;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ImportPrivateKeyRequest {

  private UTXOProvider utxoProvider;

  private String walletId;

  private String privateKey;

  private String label = "";

  private boolean rescan = true;

  public ImportPrivateKeyRequest(UTXOProvider utxoProvider, String walletId, String privateKey) {
    this.utxoProvider = utxoProvider;
    this.walletId = walletId;
    this.privateKey = privateKey;
  }

}

package com.hacks1ash.crypto.wallet.blockchain.bitcoin.model.request;

import com.hacks1ash.crypto.wallet.blockchain.factory.UTXOProvider;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateWalletRequest {

  private UTXOProvider utxoProvider;

  private String name;

  private boolean disablePrivateKeys = false;

  private boolean blank = false;

  private String passphrase = "";

  private boolean avoidReuse = false;

  private boolean descriptors = false;

  private boolean loadOnStartup = true;

  public CreateWalletRequest(UTXOProvider utxoProvider, String name, boolean blank) {
    this.utxoProvider = utxoProvider;
    this.name = name;
    this.blank = blank;
  }

}

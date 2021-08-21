package com.hacks1ash.crypto.wallet.blockchain.bitcoin.model.request;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
public class CreateWalletRequest {

  @NonNull
  private String name;

  private boolean disablePrivateKeys = false;

  private boolean blank = false;

  private String passphrase = "";

  private boolean avoidReuse = false;

  private boolean descriptors = false;

  private boolean loadOnStartup = true;

}

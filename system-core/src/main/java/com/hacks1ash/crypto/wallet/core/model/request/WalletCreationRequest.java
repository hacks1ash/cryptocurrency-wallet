package com.hacks1ash.crypto.wallet.core.model.request;

import lombok.Data;

@Data
public class WalletCreationRequest {

  private String name;

  private String currency;

  private String hdSeed;

}

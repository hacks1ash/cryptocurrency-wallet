package com.hacks1ash.crypto.wallet.blockchain.model;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
public class AddressWithPrivate {

  @NonNull
  private String address;

  @NonNull
  private String privateKey;

  private String redeemScript;

}
package com.hacks1ash.crypto.wallet.blockchain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddressWithPrivate {

  private String address;

  private String privateKey;

}
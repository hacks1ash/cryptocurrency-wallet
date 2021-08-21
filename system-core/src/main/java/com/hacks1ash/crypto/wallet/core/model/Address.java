package com.hacks1ash.crypto.wallet.core.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Address {

  private String name;

  private String address;

  private AddressType type;

  private int index;

}

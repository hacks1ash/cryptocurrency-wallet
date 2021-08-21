package com.hacks1ash.crypto.wallet.core.model.response;

import com.hacks1ash.crypto.wallet.core.model.AddressType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddressResponse {

  private String walletId;

  private String name;

  private String address;

  private AddressType type;

}

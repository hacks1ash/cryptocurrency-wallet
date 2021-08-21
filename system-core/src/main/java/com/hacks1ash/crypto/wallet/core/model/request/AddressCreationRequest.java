package com.hacks1ash.crypto.wallet.core.model.request;

import com.hacks1ash.crypto.wallet.core.model.AddressType;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
public class AddressCreationRequest {

  private String name;

  private AddressType addressType;

}

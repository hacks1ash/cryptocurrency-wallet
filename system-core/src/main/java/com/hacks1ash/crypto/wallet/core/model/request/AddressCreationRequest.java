package com.hacks1ash.crypto.wallet.core.model.request;

import com.hacks1ash.crypto.wallet.core.model.AddressType;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class AddressCreationRequest {

  @NotEmpty
  @NotNull
  private String name;

  @NotEmpty
  @NotNull
  private AddressType addressType;

}

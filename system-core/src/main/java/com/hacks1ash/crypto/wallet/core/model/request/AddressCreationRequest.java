package com.hacks1ash.crypto.wallet.core.model.request;

import com.hacks1ash.crypto.wallet.core.WalletException;
import com.hacks1ash.crypto.wallet.core.model.AddressType;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@EqualsAndHashCode(callSuper = true)
public class AddressCreationRequest extends AbstractRequest {

  private String name;

  private AddressType addressType = AddressType.DEFAULT;

  @Override
  public AddressCreationRequest validate() {
    if (this.name == null || this.name.isEmpty()) {
      throw new WalletException.ParameterRequired("name");
    }
    return this;
  }

}

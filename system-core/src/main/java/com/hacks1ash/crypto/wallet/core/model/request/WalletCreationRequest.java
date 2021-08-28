package com.hacks1ash.crypto.wallet.core.model.request;

import com.hacks1ash.crypto.wallet.core.WalletException;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@EqualsAndHashCode(callSuper = true)
public class WalletCreationRequest extends AbstractRequest {

  private String name;

  private String currency;

  private String hdSeed;

  @Override
  public WalletCreationRequest validate() {
    if (this.name == null || this.name.isEmpty()) {
      throw new WalletException.ParameterRequired("name");
    }

    if (this.currency == null || this.currency.isEmpty()) {
      throw new WalletException.ParameterRequired("currency");
    }

    if (this.hdSeed != null && this.hdSeed.isEmpty()) {
      throw new WalletException.InvalidParameter("hdSeed", "mustn't be blank, should contain 12 words seperated with space");
    }

    return this;
  }


}

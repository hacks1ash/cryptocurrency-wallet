package com.hacks1ash.crypto.wallet.core.model.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class WalletCreationRequest {

  @NotEmpty(message = "Name is required")
  @NotNull(message = "Name is required")
  @NotBlank(message = "Name is required")
  private String name;

  @NotEmpty(message = "Currency is required")
  @NotNull(message = "Currency is required")
  @NotBlank(message = "Currency is required")
  private String currency;

  private String hdSeed;

}

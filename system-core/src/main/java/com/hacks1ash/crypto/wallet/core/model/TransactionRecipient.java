package com.hacks1ash.crypto.wallet.core.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigInteger;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionRecipient {

  @NotEmpty
  @NotNull
  private String address;

  private BigInteger amount;

}

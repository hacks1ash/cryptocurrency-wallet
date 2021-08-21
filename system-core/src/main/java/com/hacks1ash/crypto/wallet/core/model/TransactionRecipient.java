package com.hacks1ash.crypto.wallet.core.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionRecipient {

  private String address;

  private BigInteger amount;

}

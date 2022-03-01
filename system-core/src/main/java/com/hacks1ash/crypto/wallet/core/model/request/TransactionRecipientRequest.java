package com.hacks1ash.crypto.wallet.core.model.request;

import com.hacks1ash.crypto.wallet.core.WalletException;
import lombok.Data;

import java.math.BigInteger;

@Data
public class TransactionRecipientRequest implements AbstractRequest {

  private String address;

  private BigInteger amount;

  private boolean subtractFee = false;

  public TransactionRecipientRequest validate() {
    if (address == null || address.isEmpty()) {
      throw new WalletException.ParameterRequired("address");
    }
    if (amount == null) {
      throw new WalletException.ParameterRequired("amount");
    } else if (amount.compareTo(BigInteger.valueOf(1000)) < 0) {
      throw new WalletException.InvalidParameter("amount", "must be more then 1000 satoshi");
    }
    return this;
  }


}

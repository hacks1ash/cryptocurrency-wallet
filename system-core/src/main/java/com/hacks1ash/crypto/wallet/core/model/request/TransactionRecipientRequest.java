package com.hacks1ash.crypto.wallet.core.model.request;

import com.hacks1ash.crypto.wallet.core.WalletException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

@Data
@EqualsAndHashCode(callSuper = true)
public class TransactionRecipientRequest extends AbstractRequest {

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

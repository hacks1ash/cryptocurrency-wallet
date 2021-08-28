package com.hacks1ash.crypto.wallet.core.model.request;

import com.hacks1ash.crypto.wallet.core.WalletException;
import com.hacks1ash.crypto.wallet.core.model.TransactionRecipient;
import com.hacks1ash.crypto.wallet.core.model.TransactionSpeed;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigInteger;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class TransactionRequest extends AbstractRequest {

  private List<TransactionRecipient> recipients;

  private TransactionSpeed speed;

  private BigInteger feePerByte;

  @Override
  public TransactionRequest validate() {
    if (recipients == null || recipients.isEmpty()) {
      throw new WalletException.ParameterRequired("recipients");
    } else {
      for (TransactionRecipient recipient : recipients) {
        recipient.validate();
      }
    }

    if (speed == null && feePerByte == null) {
      throw new WalletException.ParameterRequired("speed or feePerByte");
    }

    if (speed != null && feePerByte != null) {
      throw new WalletException.ParameterRequired("speed or feePerByte", "not both");
    }
    return this;
  }
}


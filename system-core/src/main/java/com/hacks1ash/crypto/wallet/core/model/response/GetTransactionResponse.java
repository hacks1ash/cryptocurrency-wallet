package com.hacks1ash.crypto.wallet.core.model.response;

import com.hacks1ash.crypto.wallet.blockchain.model.response.GetTrasactionResponse;
import com.hacks1ash.crypto.wallet.blockchain.model.response.ListTransactionResponse;
import com.hacks1ash.crypto.wallet.core.model.CryptoCurrency;
import com.hacks1ash.crypto.wallet.core.model.TransactionRecipient;
import com.hacks1ash.crypto.wallet.core.model.TransactionType;
import com.hacks1ash.crypto.wallet.core.utils.CurrencyUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetTransactionResponse {

  private String txId;

  private TransactionType type;

  private long confirmations;

  private BigInteger blockchainFee;

  private List<TransactionRecipient> participants = new ArrayList<>();

  public GetTransactionResponse(GetTrasactionResponse response, CryptoCurrency currency) {
    this.txId = response.getTxid();
    this.confirmations = response.getConfirmations();
    this.blockchainFee = response.getFee() == null ? BigInteger.ZERO : CurrencyUtils.toMinorUnit(currency, response.getFee());
    boolean isInternal = false;
    for (GetTrasactionResponse.Details detail : response.getDetails()) {
      TransactionType type = TransactionType.fromStr(detail.getCategory());
      if (this.type != null && this.type != type) {
        isInternal = true;
      }
      this.type = type;
      this.participants.add(new TransactionRecipient(detail.getAddress(), CurrencyUtils.toMinorUnit(currency, detail.getAmount())));
    }
    this.type = isInternal ? TransactionType.INTERNAL_TRANSFER : this.type;
  }

  public GetTransactionResponse(ListTransactionResponse response, CryptoCurrency currency) {
    this.txId = response.getTxId();
    this.confirmations = response.getConfirmations();
    this.blockchainFee = response.getFee() == null ? BigInteger.ZERO : CurrencyUtils.toMinorUnit(currency, response.getFee());
    this.type = TransactionType.fromStr(response.getCategory());
    this.participants = Collections.singletonList(new TransactionRecipient(response.getAddress(), CurrencyUtils.toMinorUnit(currency, response.getAmount())));
  }
}

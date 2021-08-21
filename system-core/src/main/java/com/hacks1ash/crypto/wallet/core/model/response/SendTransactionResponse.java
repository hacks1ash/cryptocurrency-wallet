package com.hacks1ash.crypto.wallet.core.model.response;

import com.hacks1ash.crypto.wallet.core.model.FeeUnit;
import com.hacks1ash.crypto.wallet.core.model.TransactionRecipient;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SendTransactionResponse {

  private String txId;

  private List<TransactionRecipient> recipients;

  private BigInteger blockchainFee;

  private FeeUnit feeUnit;

}

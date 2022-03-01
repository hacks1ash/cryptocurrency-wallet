package com.hacks1ash.crypto.wallet.blockchain.model.response;

import java.math.BigDecimal;

public interface FundRawTransactionResponse {

  String getHex();

  BigDecimal getFee();

  int getChangePosition();

}

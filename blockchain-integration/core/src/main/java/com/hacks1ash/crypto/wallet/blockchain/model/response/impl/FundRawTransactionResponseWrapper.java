package com.hacks1ash.crypto.wallet.blockchain.model.response.impl;

import com.hacks1ash.crypto.wallet.blockchain.model.response.FundRawTransactionResponse;
import com.hacks1ash.crypto.wallet.blockchain.utils.MapWrapper;

import java.math.BigDecimal;
import java.util.Map;

public class FundRawTransactionResponseWrapper extends MapWrapper implements FundRawTransactionResponse {

  public FundRawTransactionResponseWrapper(Map<String, ?> m) {
    super(m);
  }

  @Override
  public String getHex() {
    return mapStr("hex");
  }

  @Override
  public BigDecimal getFee() {
    return mapBigDecimal("fee");
  }

  @Override
  public int getChangePosition() {
    return mapInt("changepos");
  }
}

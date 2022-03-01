package com.hacks1ash.crypto.wallet.blockchain.model.response.impl;

import com.hacks1ash.crypto.wallet.blockchain.model.response.EstimateSmartFeeResponse;
import com.hacks1ash.crypto.wallet.blockchain.utils.MapWrapper;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Map;

public class EstimateSmartFeeResponseWrapper extends MapWrapper implements EstimateSmartFeeResponse, Serializable {

  public EstimateSmartFeeResponseWrapper(Map<String, ?> m) {
    super(m);
  }

  @Override
  public BigDecimal getFeeRate() {
    return mapBigDecimal("feerate");
  }

  @Override
  public int getConfTargetBlock() {
    return mapInt("blocks");
  }
}

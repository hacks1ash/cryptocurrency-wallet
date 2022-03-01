package com.hacks1ash.crypto.wallet.blockchain.model.response;

import java.math.BigDecimal;

public interface EstimateSmartFeeResponse {

  BigDecimal getFeeRate();

  int getConfTargetBlock();

}

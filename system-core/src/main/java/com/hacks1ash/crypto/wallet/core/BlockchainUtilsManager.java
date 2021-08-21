package com.hacks1ash.crypto.wallet.core;

import com.hacks1ash.crypto.wallet.core.model.response.SmartFeeResponse;

public interface BlockchainUtilsManager {

  SmartFeeResponse getSmartFee(String currency, int confBlockTarget);

}

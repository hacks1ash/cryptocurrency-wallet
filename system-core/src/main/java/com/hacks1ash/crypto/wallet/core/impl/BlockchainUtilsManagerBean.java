package com.hacks1ash.crypto.wallet.core.impl;

import com.hacks1ash.crypto.wallet.blockchain.UTXORPCClient;
import com.hacks1ash.crypto.wallet.blockchain.bitcoin.model.request.EstimateSmartFeeRequest;
import com.hacks1ash.crypto.wallet.blockchain.bitcoin.model.response.EstimateSmartFeeResponse;
import com.hacks1ash.crypto.wallet.core.BlockchainUtilsManager;
import com.hacks1ash.crypto.wallet.core.model.CryptoCurrency;
import com.hacks1ash.crypto.wallet.core.model.response.SmartFeeResponse;
import com.hacks1ash.crypto.wallet.core.utils.BlockchainIntegrationFactory;
import com.hacks1ash.crypto.wallet.core.utils.CurrencyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BlockchainUtilsManagerBean implements BlockchainUtilsManager {

  private BlockchainIntegrationFactory blockchainFactory;

  @Override
  public SmartFeeResponse getSmartFee(String currency, int confBlockTarget) {
    CryptoCurrency cryptoCurrency = CryptoCurrency.cryptoCurrencyFromShortName(currency);
    UTXORPCClient rpcClient = blockchainFactory.getRPCClient(cryptoCurrency);
    EstimateSmartFeeResponse response = rpcClient.estimateSmartFee(new EstimateSmartFeeRequest(confBlockTarget));
    return new SmartFeeResponse(
      CurrencyUtils.toMinorUnit(cryptoCurrency, response.getFeeRate()),
      response.getConfTargetBlock(),
      cryptoCurrency.getFeeUnit()
    );
  }

  @Autowired
  public void setBlockchainFactory(BlockchainIntegrationFactory blockchainFactory) {
    this.blockchainFactory = blockchainFactory;
  }
}

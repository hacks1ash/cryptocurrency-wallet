package com.hacks1ash.crypto.wallet.rest;

import co.elastic.apm.api.CaptureTransaction;
import com.hacks1ash.crypto.wallet.core.BlockchainUtilsManager;
import com.hacks1ash.crypto.wallet.core.model.response.SmartFeeResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "utils/")
public class UtilsController {

  private BlockchainUtilsManager blockchainUtilsManager;

  @RequestMapping(value = "{currency}/smartFee", method = RequestMethod.GET)
  @CaptureTransaction
  public SmartFeeResponse getSmartFee(@PathVariable String currency, @RequestParam("confBlockTarget") int confBlockTarget) {
    return blockchainUtilsManager.getSmartFee(currency, confBlockTarget);
  }

  @Autowired
  public void setBlockchainUtilsManager(BlockchainUtilsManager blockchainUtilsManager) {
    this.blockchainUtilsManager = blockchainUtilsManager;
  }
}

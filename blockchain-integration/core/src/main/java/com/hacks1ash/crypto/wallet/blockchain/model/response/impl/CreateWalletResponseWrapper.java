package com.hacks1ash.crypto.wallet.blockchain.model.response.impl;

import com.hacks1ash.crypto.wallet.blockchain.model.response.CreateWalletResponse;
import com.hacks1ash.crypto.wallet.blockchain.utils.MapWrapper;

import java.io.Serializable;
import java.util.Map;

public class CreateWalletResponseWrapper extends MapWrapper implements CreateWalletResponse, Serializable {

  public CreateWalletResponseWrapper(Map<String, ?> m) {
    super(m);
  }

  @Override
  public String getName() {
    return mapStr("name");
  }

  @Override
  public String getWarning() {
    return mapStr("warning");
  }
}

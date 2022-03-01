package com.hacks1ash.crypto.wallet.core.model.request;

import com.hacks1ash.crypto.wallet.core.WalletException;
import lombok.Data;

@Data
public class WebhookCreationRequest implements AbstractRequest {

  private String url;

  private Long confirmationTarget;

  @Override
  public WebhookCreationRequest validate() {
    if (this.url == null || this.url.isEmpty()) {
      throw new WalletException.ParameterRequired("url");
    }

    return this;
  }

}

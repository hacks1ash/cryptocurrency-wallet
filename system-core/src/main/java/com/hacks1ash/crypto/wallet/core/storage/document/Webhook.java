package com.hacks1ash.crypto.wallet.core.storage.document;

import com.hacks1ash.crypto.wallet.core.model.WebhookStatus;
import com.hacks1ash.crypto.wallet.core.model.WebhookTXStatus;
import com.hacks1ash.crypto.wallet.core.model.response.GetTransactionResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Webhook extends AbstractDocument {

  private String walletId;

  private String txId;

  private boolean isReplaceable;

  private boolean isParentConfirmed;

  private long confirmations;

  private long blockHeight;

  private WebhookTXStatus webhookTXStatus;

  private WebhookStatus webhookStatus;

  private GetTransactionResponse webhookData;


}

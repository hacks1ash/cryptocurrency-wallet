package com.hacks1ash.crypto.wallet.core.model.request;

import com.hacks1ash.crypto.wallet.core.model.TransactionRecipient;
import com.hacks1ash.crypto.wallet.core.model.TransactionSpeed;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
public class TransactionRequest {

  private List<TransactionRecipient> recipients;

  private TransactionSpeed speed;

}


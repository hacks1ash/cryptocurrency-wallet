package com.hacks1ash.crypto.wallet.core.model.request;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class NewBlock {

  private long blockNumber;

  private String coin;

}

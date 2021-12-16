package com.hacks1ash.crypto.wallet.core.model.request;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class NewTransaction {

  private String txid;

  private List<String> addresses;

  private int blockHeight;

  private String coin;

}

package com.hacks1ash.crypto.wallet.blockchain.model;

public class BatchParam {
  public final String id;
  public final Object[] params;

  public BatchParam(String id, Object[] params) {
    this.id=id;
    this.params=params;
  }
}

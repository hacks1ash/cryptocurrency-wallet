package com.hacks1ash.crypto.wallet.core.model;

public enum TransactionSpeed {

  FAST(2),
  MEDIUM(6),
  SLOW(12);

  private final int blockSize;

  TransactionSpeed(int blockSize) {
    this.blockSize = blockSize;
  }

  public int getBlockSize() {
    return blockSize;
  }
}

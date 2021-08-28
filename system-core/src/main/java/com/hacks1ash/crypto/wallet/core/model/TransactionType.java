package com.hacks1ash.crypto.wallet.core.model;

import com.hacks1ash.crypto.wallet.core.WalletException;

public enum TransactionType {

  SEND, RECEIVE, INTERNAL_TRANSFER;

  public static TransactionType fromStr(String nodeStr) {
    for (TransactionType type :TransactionType.values()) {
      if (type.name().equalsIgnoreCase(nodeStr)) {
        return type;
      }
    }
    throw new WalletException.UnknownTransactionType(nodeStr);
  }
}

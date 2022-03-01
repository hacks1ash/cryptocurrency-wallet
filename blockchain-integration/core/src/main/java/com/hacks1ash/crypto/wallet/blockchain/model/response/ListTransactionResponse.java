package com.hacks1ash.crypto.wallet.blockchain.model.response;

import java.math.BigDecimal;

public interface ListTransactionResponse {

  boolean involvesWatchOnly();

  String getAddress();

  String getCategory();

  BigDecimal getAmount();

  String getLabel();

  int getVout();

  BigDecimal getFee();

  long getConfirmations();

  boolean isGenerated();

  boolean isTrusted();

  String getBlockHash();

  long getBlockHeight();

  long getBlockIndex();

  long getBlockTime();

  String getTxId();

//  List<String> getWalletConflicts();

  long getTime();

  long getTimeReceived();

  String getComment();

  boolean isReplaceable();

  boolean isAbandoned();

}

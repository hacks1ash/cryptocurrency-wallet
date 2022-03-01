package com.hacks1ash.crypto.wallet.blockchain.model.response;

import java.math.BigDecimal;
import java.util.List;

public interface GetTrasactionResponse {

  BigDecimal getAmount();

  BigDecimal getFee();

  long getConfirmations();

  boolean isGenerated();

  boolean isTrusted();

  String getBlockHash();

  long getBlockHeight();

  long getBlockIndex();

  long getBlockTime();

  String getTxid();

  long getTime();

  long getTimeReceived();

  String getComment();

  boolean isReplaceable();

  String getHex();

  List<Details> getDetails();

  public interface Details {

    boolean involvesWatchonly();

    String getAddress();

    String getCategory();

    BigDecimal getAmount();

    String getLabel();

    int getVout();

    BigDecimal getFee();

    boolean isAbandoned();
  }

}

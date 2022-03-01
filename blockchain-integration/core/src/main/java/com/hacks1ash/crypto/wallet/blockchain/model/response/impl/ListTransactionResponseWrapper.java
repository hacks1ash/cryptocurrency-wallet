package com.hacks1ash.crypto.wallet.blockchain.model.response.impl;

import com.hacks1ash.crypto.wallet.blockchain.model.response.ListTransactionResponse;
import com.hacks1ash.crypto.wallet.blockchain.utils.MapWrapper;

import java.math.BigDecimal;
import java.util.Map;

public class ListTransactionResponseWrapper extends MapWrapper implements ListTransactionResponse {
  public ListTransactionResponseWrapper(Map<String, ?> m) {
    super(m);
  }

  @Override
  public boolean involvesWatchOnly() {
    return mapBool("involvesWatchonly");
  }

  @Override
  public String getAddress() {
    return mapStr("address");
  }

  @Override
  public String getCategory() {
    return mapStr("category");
  }

  @Override
  public BigDecimal getAmount() {
    return mapBigDecimal("amount");
  }

  @Override
  public String getLabel() {
    return mapStr("label");
  }

  @Override
  public int getVout() {
    return mapInt("vout");
  }

  @Override
  public BigDecimal getFee() {
    return mapBigDecimal("fee");
  }

  @Override
  public long getConfirmations() {
    return mapLong("confirmations");
  }

  @Override
  public boolean isGenerated() {
    return mapBool("generated");
  }

  @Override
  public boolean isTrusted() {
    return mapBool("trusted");
  }

  @Override
  public String getBlockHash() {
    return mapStr("blockhash");
  }

  @Override
  public long getBlockHeight() {
    return mapLong("blockheight");
  }

  @Override
  public long getBlockIndex() {
    return mapLong("blockindex");
  }

  @Override
  public long getBlockTime() {
    return mapLong("blocktime");
  }

  @Override
  public String getTxId() {
    return mapStr("txid");
  }

  @Override
  public long getTime() {
    return mapLong("time");
  }

  @Override
  public long getTimeReceived() {
    return mapLong("timereceived");
  }

  @Override
  public String getComment() {
    return mapStr("comment");
  }

  @Override
  public boolean isReplaceable() {
    return mapBool("bip125-replaceable");
  }

  @Override
  public boolean isAbandoned() {
    return mapBool("abandoned");
  }
}

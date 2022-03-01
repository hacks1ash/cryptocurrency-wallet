package com.hacks1ash.crypto.wallet.blockchain.model.response.impl;

import com.hacks1ash.crypto.wallet.blockchain.model.response.GetTrasactionResponse;
import com.hacks1ash.crypto.wallet.blockchain.utils.ListMapWrapper;
import com.hacks1ash.crypto.wallet.blockchain.utils.MapWrapper;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class GetTrasactionResponseWrapper extends MapWrapper implements GetTrasactionResponse {

  public GetTrasactionResponseWrapper(Map<String, ?> m) {
    super(m);
  }

  @Override
  public BigDecimal getAmount() {
    return mapBigDecimal("amount");
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
    Long blockheight = mapLong("blockheight");
    if (blockheight != null) {
      return blockheight;
    } else {
      return -1;
    }
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
  public String getTxid() {
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
    return mapStr("bip125-replaceable") == "yes";
  }

  @Override
  public String getHex() {
    return mapStr("hex");
  }

  @Override
  @SuppressWarnings({"unchecked", "unsafe"})
  public List<Details> getDetails() {
    return new ListMapWrapper<Details>((List<Map<String, ?>>) m.get("details")) {
      @Override
      protected Details wrap(Map<String, ?> m) {
        return new DetailsWrapper(m);
      }
    };
  }

  public static class DetailsWrapper extends MapWrapper implements Details {

    public DetailsWrapper(Map<String, ?> m) {
      super(m);
    }

    @Override
    public boolean involvesWatchonly() {
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
    public boolean isAbandoned() {
      return mapBool("abandoned");
    }
  }
}

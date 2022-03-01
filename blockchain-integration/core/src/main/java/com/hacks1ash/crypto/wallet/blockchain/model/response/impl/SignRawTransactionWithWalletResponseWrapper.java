package com.hacks1ash.crypto.wallet.blockchain.model.response.impl;

import com.hacks1ash.crypto.wallet.blockchain.model.response.SignRawTransactionWithWalletResponse;
import com.hacks1ash.crypto.wallet.blockchain.utils.ListMapWrapper;
import com.hacks1ash.crypto.wallet.blockchain.utils.MapWrapper;

import java.util.List;
import java.util.Map;

public class SignRawTransactionWithWalletResponseWrapper extends MapWrapper implements SignRawTransactionWithWalletResponse {

  public SignRawTransactionWithWalletResponseWrapper(Map<String, ?> m) {
    super(m);
  }

  @Override
  public String getTxHex() {
    return mapStr("hex");
  }

  @Override
  public boolean isComplete() {
    return mapBool("complete");
  }

  @Override
  @SuppressWarnings({"unchecked",  "unsafe"})
  public List<Error> getErrors() {
    return new ListMapWrapper<>((List<Map<String, ?>>) m.get("errors")) {
      @Override
      protected Error wrap(Map<String, ?> m) {
        return new ErrorWrapper(m);
      }
    };
  }

  public static class ErrorWrapper extends MapWrapper implements Error {

    public ErrorWrapper(Map<String, ?> m) {
      super(m);
    }

    @Override
    public String getTxid() {
      return mapStr("txid");
    }

    @Override
    public int getVout() {
      return mapInt("vout");
    }

    @Override
    public String getScriptSig() {
      return mapStr("scriptSig");
    }

    @Override
    public int getSequence() {
      return mapInt("sequence");
    }

    @Override
    public String getError() {
      return mapStr("error");
    }
  }
}

package com.hacks1ash.crypto.wallet.blockchain.bitcoin.model.response;

import java.util.List;

public interface SignRawTransactionWithWalletResponse {

  String getTxHex();

  boolean isComplete();

  List<Error> getErrors();

  public interface Error {

    String getTxid();

    int getVout();

    String getScriptSig();

    int getSequence();

    String getError();
  }

}

package com.hacks1ash.crypto.wallet.core.model;

import java.util.Arrays;
import java.util.List;

public enum AddressType {

  P2PKH(Arrays.asList(CryptoCurrency.BITCOIN, CryptoCurrency.TEST_BITCOIN)),
  P2SH(Arrays.asList(CryptoCurrency.BITCOIN, CryptoCurrency.TEST_BITCOIN)),
  BECH_32(Arrays.asList(CryptoCurrency.BITCOIN, CryptoCurrency.TEST_BITCOIN)),
  DEFAULT(Arrays.asList(CryptoCurrency.BITCOIN, CryptoCurrency.TEST_BITCOIN));

  private final List<CryptoCurrency> supportedCoinList;

  AddressType(List<CryptoCurrency> supportedCoinList) {
    this.supportedCoinList = supportedCoinList;
  }

  public List<CryptoCurrency> getSupportedCoinList() {
    return supportedCoinList;
  }
}

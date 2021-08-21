package com.hacks1ash.crypto.wallet.core.utils;

import com.hacks1ash.crypto.wallet.core.model.CryptoCurrency;

import java.math.BigDecimal;
import java.math.BigInteger;

public class CurrencyUtils {

  public static BigInteger toMinorUnit(CryptoCurrency currency, BigDecimal amount) {
    return amount.movePointRight(currency.getDecimalPoints()).toBigInteger();
  }

  public static BigDecimal toMajorUnit(CryptoCurrency currency, BigInteger amount) {
    return new BigDecimal(amount).movePointLeft(currency.getDecimalPoints());
  }
}

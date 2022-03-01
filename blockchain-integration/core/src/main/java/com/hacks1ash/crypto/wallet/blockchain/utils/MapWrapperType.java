package com.hacks1ash.crypto.wallet.blockchain.utils;

import java.math.BigDecimal;
import java.util.Date;

public interface MapWrapperType {

  Boolean mapBool(String key);

  Integer mapInt(String key);

  Long mapLong(String key);

  String mapStr(String key);

  Date mapDate(String key);

  BigDecimal mapBigDecimal(String key);

  byte[] mapHex(String key);
}
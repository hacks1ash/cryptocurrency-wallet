package com.hacks1ash.crypto.wallet.blockchain.utils;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

public class MapWrapper implements MapWrapperType {

  protected final Map<String, ?> m;

  protected MapWrapper(Map<String, ?> m) {
    this.m = m;
  }

  @Override
  public Boolean mapBool(String key) {
    return mapBool(m, key);
  }

  @Override
  public Integer mapInt(String key) {
    return mapInt(m, key);
  }

  @Override
  public Long mapLong(String key) {
    return mapLong(m, key);
  }

  @Override
  public String mapStr(String key) {
    return mapStr(m, key);
  }

  @Override
  public Date mapDate(String key) {
    return mapDate(m, key);
  }

  @Override
  public BigDecimal mapBigDecimal(String key) {
    return mapBigDecimal(m, key);
  }

  @Override
  public byte[] mapHex(String key) {
    return mapHex(m, key);
  }

  private static Boolean mapBool(Map<String, ?> m, String key) {
    Object val = m.get(key);
    if (!(val instanceof Boolean)) return null;
    return (Boolean) val;
  }

  private static BigDecimal mapBigDecimal(Map<String, ?> m, String key) {
    Object val = m.get(key);
    if (val instanceof BigDecimal) return (BigDecimal) val;
    String strVal = mapStr(m, key);
    if (strVal == null) return null;
    return new BigDecimal(strVal);
  }

  private static Integer mapInt(Map<String, ?> m, String key) {
    Object val = m.get(key);
    if (!(val instanceof Number)) return null;
    return ((Number) val).intValue();
  }

  private static Long mapLong(Map<String, ?> m, String key) {
    Object val = m.get(key);
    if (!(val instanceof Number)) return null;
    return ((Number) val).longValue();
  }

  private static String mapStr(Map<String, ?> m, String key) {
    Object val = m.get(key);
    if (val == null) return null;
    return val.toString();
  }

  private static Date mapDate(Map<String, ?> m, String key) {
    Long longVal = mapLong(m, key);
    if (longVal == null) return null;
    return new Date(longVal * 1000);
  }

  private static byte[] mapHex(Map<String, ?> m, String key) {
    String strVal = mapStr(m, key);
    if (strVal == null) return null;
    return HexCoder.decode(strVal);
  }

  @Override
  public String toString() {
    return String.valueOf(m);
  }

}
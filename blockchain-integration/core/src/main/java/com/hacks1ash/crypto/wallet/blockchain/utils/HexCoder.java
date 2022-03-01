package com.hacks1ash.crypto.wallet.blockchain.utils;

public final class HexCoder {

  private final static char[] hexArray = "0123456789abcdef".toCharArray();

  // Hide ctor
  private HexCoder() {
  }

  public static String encode(byte[] bytes) {
    char[] hexChars = new char[bytes.length * 2];
    for (int i = 0; i < bytes.length; i++) {
      int v = bytes[i] & 0xFF;
      hexChars[i * 2] = hexArray[v >>> 4];
      hexChars[i * 2 + 1] = hexArray[v & 0x0F];
    }
    return new String(hexChars);
  }

  public static byte[] decode(String encoded) {
    char[] hexChars = encoded.toLowerCase().toCharArray();
    byte[] bytes = new byte[hexChars.length / 2];
    for (int i = 0; i < bytes.length; i++) {
      char v1 = hexChars[2 * i];
      if ('0' <= v1 && v1 <= '9') {
        bytes[i] = (byte) (16 * (v1 - '0'));
      } else {
        bytes[i] = (byte) (16 * (10 + v1 - 'a'));
      }
      char v2 = hexChars[2 * i + 1];
      if ('0' <= v2 && v2 <= '9') {
        bytes[i] += (byte) (v2 - '0');
      } else {
        bytes[i] += (byte) (10 + v2 - 'a');
      }
    }
    return bytes;
  }
}
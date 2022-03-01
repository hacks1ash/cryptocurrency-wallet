package com.hacks1ash.crypto.wallet.blockchain.model;

import lombok.Getter;

import java.util.Map;

@Getter
public class RPCError {
  private int code;
  private String message;
  private final Object id;

  @SuppressWarnings({"rawtypes"})
  public RPCError(Map errorResponse) {
    this.id = errorResponse.get("id");
    Map error = (Map) errorResponse.get("error");
    String message = (String) errorResponse.get("message");
    if (error != null) {
      Number n = (Number) error.get("code");
      this.code = n != null ? n.intValue() : 0;
      this.message = (String) error.get("message");
    }
    if (error == null && message != null) {
      Number n = (Number) errorResponse.get("code");
      this.message = message;
      this.code = n != null ? n.intValue() : 0;
    }
  }

  @Override
  public String toString() {
    return "BitcoinRPCError{" +
      "code=" + code +
      ", message='" + message + '\'' +
      ", id=" + id +
      '}';
  }
}
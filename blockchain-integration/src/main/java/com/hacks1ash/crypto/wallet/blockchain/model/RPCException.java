package com.hacks1ash.crypto.wallet.blockchain.model;

import com.hacks1ash.crypto.wallet.blockchain.GenericRpcException;
import com.hacks1ash.crypto.wallet.blockchain.utils.JSON;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Slf4j
@Getter
public class RPCException extends GenericRpcException {

  private String rpcMethod;
  private String rpcParams;
  private int responseCode;
  private String responseMessage;
  private String response;
  private RPCError rpcError;

  /**
   * Creates a new instance of <code>BitcoinRPCException</code> with response
   * detail.
   *
   * @param method          the rpc method called
   * @param params          the parameters sent
   * @param responseCode    the HTTP code received
   * @param responseMessage the HTTP response message
   * @param response        the error stream received
   */
  @SuppressWarnings("rawtypes")
  public RPCException(String method,
                      String params,
                      int responseCode,
                      String responseMessage,
                      String response) {
    super(((Map) ((Map) JSON.parse(response)).get("error")) == null && responseCode == 500 ? responseMessage : new RPCError((Map) ((Map) JSON.parse(response)).get("error")).getMessage());
    this.rpcMethod = method;
    this.rpcParams = params;
    this.responseCode = responseCode;
    this.responseMessage = responseMessage;
    this.response = response;
    if (responseCode == 500) {
      // Bitcoind application error when handle the request
      // extract code/message for callers to handle
      Map error = (Map) ((Map) JSON.parse(response)).get("error");
      if (error != null) {
        rpcError = new RPCError(error);
      }
    }
  }

  public RPCException(String method, String params, Throwable cause) {
    super("RPC Query Failed (method: " + method + ", params: " + params + ")", cause);
    this.rpcMethod = method;
    this.rpcParams = params;
  }

  public RPCException(String msg) {
    super(msg);
  }

  public RPCException(RPCError error) {
    super(error.getMessage());
    this.rpcError = error;
  }
  public RPCException(RPCError error, String message) {
    super(message);
    this.rpcError = error;
  }
}
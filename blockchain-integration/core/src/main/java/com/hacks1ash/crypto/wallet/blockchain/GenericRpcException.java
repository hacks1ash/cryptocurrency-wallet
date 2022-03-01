package com.hacks1ash.crypto.wallet.blockchain;

import lombok.Getter;

@Getter
public class GenericRpcException extends RuntimeException {

    private final String errorKey;

    private final String errorMessage;

    private final int errorCode;

    public GenericRpcException(String errorKey, String errorMessage, int errorCode) {
        super(errorMessage);
        this.errorKey = errorKey;
        this.errorMessage = errorMessage;
        this.errorCode = errorCode;
    }

    public GenericRpcException(Throwable cause, String errorKey, String errorMessage, int errorCode) {
        super(errorMessage, cause);
        this.errorKey = errorKey;
        this.errorMessage = errorMessage;
        this.errorCode = errorCode;
    }

    public GenericRpcException(String msg) {
        this("rpc.error", msg, 500);
    }

    public GenericRpcException(Throwable cause) {
        this(cause, "rpc.error", "RPC Server Error", 500);
    }

    public GenericRpcException(String message, Throwable cause) {
        this(cause, "rpc.error", message, 500);
    }

}
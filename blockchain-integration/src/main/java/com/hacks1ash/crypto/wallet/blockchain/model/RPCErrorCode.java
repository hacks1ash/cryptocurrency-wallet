package com.hacks1ash.crypto.wallet.blockchain.model;

public class RPCErrorCode {
    public static final int RPC_MISC_ERROR                  = -1 ;  //!< std::exception thrown in command handling
    public static final int RPC_FORBIDDEN_BY_SAFE_MODE      = -2 ;  //!< Server is in safe mode, and command is not allowed in safe mode
    public static final int RPC_TYPE_ERROR                  = -3 ;  //!< Unexpected type was passed as parameter
    public static final int RPC_INVALID_ADDRESS_OR_KEY      = -5 ;  //!< Invalid address or key
    public static final int RPC_OUT_OF_MEMORY               = -7 ;  //!< Ran out of memory during operation
    public static final int RPC_INVALID_PARAMETER           = -8 ;  //!< Invalid, missing or duplicate parameter
    public static final int RPC_DATABASE_ERROR              = -20; //!< Database error
    public static final int RPC_DESERIALIZATION_ERROR       = -22; //!< Error parsing or validating structure in raw format
    public static final int RPC_VERIFY_ERROR                = -25; //!< General error during transaction or block submission
    public static final int RPC_VERIFY_REJECTED             = -26; //!< Transaction or block was rejected by network rules
    public static final int RPC_VERIFY_ALREADY_IN_CHAIN     = -27; //!< Transaction already in chain
    public static final int RPC_IN_WARMUP                   = -28; //!< Client still warming up

}
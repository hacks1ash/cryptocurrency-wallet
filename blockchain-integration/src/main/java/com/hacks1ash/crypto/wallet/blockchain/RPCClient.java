package com.hacks1ash.crypto.wallet.blockchain;

import com.hacks1ash.crypto.wallet.blockchain.model.BatchParam;

import java.io.InputStream;
import java.util.List;

public interface RPCClient {

  Object query(String walletId, String method, Object... o) throws GenericRpcException;

  InputStream queryForStream(String walletId, String method, Object... o) throws GenericRpcException;

  Object batchQuery(String walletId, String method, List<BatchParam> batchParams) throws GenericRpcException;

}
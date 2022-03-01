package com.hacks1ash.crypto.wallet.blockchain;

import com.hacks1ash.crypto.wallet.blockchain.factory.UTXOProvider;
import com.hacks1ash.crypto.wallet.blockchain.model.BatchParam;
import com.hacks1ash.crypto.wallet.blockchain.model.NetworkParams;

import java.io.InputStream;
import java.util.List;

public interface RPCClient {

  Object query(UTXOProvider utxoProvider, String walletId, String method, NetworkParams networkParams, Object... o) throws GenericRpcException;

  InputStream queryForStream(UTXOProvider utxoProvider, String walletId, String method, NetworkParams networkParams, Object... o) throws GenericRpcException;

  Object batchQuery(UTXOProvider utxoProvider, String walletId, String method, List<BatchParam> batchParams, NetworkParams networkParams) throws GenericRpcException;

}
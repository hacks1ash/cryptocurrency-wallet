package com.hacks1ash.crypto.wallet.blockchain;

import com.hacks1ash.crypto.wallet.blockchain.bitcoin.model.request.CreateWalletRequest;
import com.hacks1ash.crypto.wallet.blockchain.bitcoin.model.request.EstimateSmartFeeRequest;
import com.hacks1ash.crypto.wallet.blockchain.bitcoin.model.request.GetBalanceRequest;
import com.hacks1ash.crypto.wallet.blockchain.bitcoin.model.request.ImportPrivateKeyRequest;
import com.hacks1ash.crypto.wallet.blockchain.bitcoin.model.response.CreateWalletResponse;
import com.hacks1ash.crypto.wallet.blockchain.bitcoin.model.response.EstimateSmartFeeResponse;

import java.math.BigDecimal;

public interface UTXORPCClient {

  CreateWalletResponse createWallet(CreateWalletRequest request);

  BigDecimal getBalance(GetBalanceRequest request);

  EstimateSmartFeeResponse estimateSmartFee(EstimateSmartFeeRequest request);

  void importPrivateKey(ImportPrivateKeyRequest request);
}

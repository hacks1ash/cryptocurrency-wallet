package com.hacks1ash.crypto.wallet.core;

import com.hacks1ash.crypto.wallet.core.model.request.AddressCreationRequest;
import com.hacks1ash.crypto.wallet.core.model.request.TransactionRequest;
import com.hacks1ash.crypto.wallet.core.model.request.WalletCreationRequest;
import com.hacks1ash.crypto.wallet.core.model.response.AddressResponse;
import com.hacks1ash.crypto.wallet.core.model.response.EstimateFeeResponse;
import com.hacks1ash.crypto.wallet.core.model.response.SendTransactionResponse;
import com.hacks1ash.crypto.wallet.core.model.response.WalletResponse;

import java.math.BigInteger;
import java.util.List;

public interface WalletManager {

  WalletResponse createWallet(WalletCreationRequest request);

  List<WalletResponse> listWallets();

  BigInteger getBalance(String walletId);

  AddressResponse createAddress(String walletId, AddressCreationRequest request);

  List<AddressResponse> getAddresses(String walletId);

  EstimateFeeResponse estimateFee(String walletId, TransactionRequest request);

  SendTransactionResponse sendTransaction(String walletId, TransactionRequest request);
}

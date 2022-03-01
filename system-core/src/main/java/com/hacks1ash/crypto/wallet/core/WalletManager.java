package com.hacks1ash.crypto.wallet.core;

import com.hacks1ash.crypto.wallet.core.model.request.AddressCreationRequest;
import com.hacks1ash.crypto.wallet.core.model.request.TransactionRequest;
import com.hacks1ash.crypto.wallet.core.model.request.WalletCreationRequest;
import com.hacks1ash.crypto.wallet.core.model.request.WebhookCreationRequest;
import com.hacks1ash.crypto.wallet.core.model.response.*;

import java.math.BigInteger;
import java.util.List;

public interface WalletManager {

  WalletResponse createWallet(WalletCreationRequest request);

  List<WalletResponse> listWallets();

  BigInteger getBalance(String walletId);

  AddressResponse createAddress(String walletId, AddressCreationRequest request);

  List<AddressResponse> getAddresses(String walletId);

  EstimateFeeResponse estimateFee(String walletId, TransactionRequest request);

  // TODO add password while sending tx
  SendTransactionResponse sendTransaction(String walletId, TransactionRequest request);

  GetTransactionResponse getTransaction(String walletId, String txId);

  List<GetTransactionResponse> getTransactions(String walletId);

  WebhookResponse createWebhook(String walletId, WebhookCreationRequest validate);

  List<WebhookResponse> getWebhooks(String walletId);

}

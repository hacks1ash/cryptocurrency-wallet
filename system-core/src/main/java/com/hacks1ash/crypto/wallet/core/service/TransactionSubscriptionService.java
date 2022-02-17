package com.hacks1ash.crypto.wallet.core.service;

import com.hacks1ash.crypto.wallet.blockchain.UTXORPCClient;
import com.hacks1ash.crypto.wallet.blockchain.bitcoin.model.request.GetTransactionRequest;
import com.hacks1ash.crypto.wallet.blockchain.bitcoin.model.response.GetTrasactionResponse;
import com.hacks1ash.crypto.wallet.blockchain.factory.UTXOClientFactory;
import com.hacks1ash.crypto.wallet.core.TransactionListener;
import com.hacks1ash.crypto.wallet.core.model.CryptoCurrency;
import com.hacks1ash.crypto.wallet.core.model.WebhookStatus;
import com.hacks1ash.crypto.wallet.core.model.WebhookTXStatus;
import com.hacks1ash.crypto.wallet.core.model.request.NewTransaction;
import com.hacks1ash.crypto.wallet.core.storage.CurrencyConfigRepository;
import com.hacks1ash.crypto.wallet.core.storage.WalletRepository;
import com.hacks1ash.crypto.wallet.core.storage.document.CurrencyConfig;
import com.hacks1ash.crypto.wallet.core.storage.document.Wallet;
import com.hacks1ash.crypto.wallet.core.storage.document.Webhook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class TransactionSubscriptionService implements TransactionListener {

  private WalletRepository walletRepository;

  private CurrencyConfigRepository currencyConfigRepository;

  private UTXOClientFactory utxoClientFactory;


  @Override
  public void onTransaction(NewTransaction newTransaction) {
    CryptoCurrency cryptoCurrency = CryptoCurrency.cryptoCurrencyFromShortName(newTransaction.getCoin());
    UTXORPCClient rpcClient = utxoClientFactory.getClient(cryptoCurrency.getUtxoProvider());

    Optional<CurrencyConfig> currencyConfig = currencyConfigRepository.findByCurrency(cryptoCurrency);
    int confirmationTarget = currencyConfig.map(CurrencyConfig::getNumberOfConfirmationsRequired).orElse(6);

    Set<Wallet> walletsByDepositAddresses = walletRepository.findAllByAddresses(newTransaction.getAddresses());
    Set<Wallet> walletsByChangeAddresses = walletRepository.findAllByChangeAddresses(newTransaction.getAddresses());

    if (walletsByDepositAddresses == null || walletsByDepositAddresses.size() == 0) {
      return;
    }

    List<Wallet> wallets = new ArrayList<>(walletsByDepositAddresses);
    Wallet wallet = wallets.get(0);
    GetTrasactionResponse transaction = rpcClient.getTransaction(new GetTransactionRequest(cryptoCurrency.getUtxoProvider(), wallet.getNodeWalletNameAlias(), newTransaction.getTxid()));

    WebhookTXStatus txStatus;

    if (transaction.getConfirmations() == 0) {
      txStatus = WebhookTXStatus.MEMPOOL;
    } else if (transaction.getConfirmations() < confirmationTarget) {
      txStatus = WebhookTXStatus.PENDING;
    } else {
      txStatus = WebhookTXStatus.CONFIRMED;
    }

    Webhook webhook = new Webhook(
      wallet.getId(),
      transaction.getTxid(),
      transaction.isReplaceable(),
      // TODO: Need to check if parent is confirmed
      true,
      transaction.getConfirmations(),
      transaction.getBlockHeight(),
      txStatus,
      WebhookStatus.WAITING_FOR_CONFIRM
    );

  }


  @Autowired
  public void setWalletRepository(WalletRepository walletRepository) {
    this.walletRepository = walletRepository;
  }

  @Autowired
  public void setCurrencyConfigRepository(CurrencyConfigRepository currencyConfigRepository) {
    this.currencyConfigRepository = currencyConfigRepository;
  }

  @Autowired
  public void setBlockchainIntegrationFactory(UTXOClientFactory utxoClientFactory) {
    this.utxoClientFactory = utxoClientFactory;
  }
}

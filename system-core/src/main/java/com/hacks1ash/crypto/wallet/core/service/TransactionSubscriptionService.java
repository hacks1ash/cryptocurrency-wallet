package com.hacks1ash.crypto.wallet.core.service;

import com.hacks1ash.crypto.wallet.blockchain.UTXORPCClient;
import com.hacks1ash.crypto.wallet.blockchain.factory.UTXOClientFactory;
import com.hacks1ash.crypto.wallet.blockchain.model.request.GetTransactionRequest;
import com.hacks1ash.crypto.wallet.blockchain.model.response.GetTrasactionResponse;
import com.hacks1ash.crypto.wallet.core.TransactionListener;
import com.hacks1ash.crypto.wallet.core.WalletException;
import com.hacks1ash.crypto.wallet.core.WalletManager;
import com.hacks1ash.crypto.wallet.core.model.CryptoCurrency;
import com.hacks1ash.crypto.wallet.core.model.WebhookStatus;
import com.hacks1ash.crypto.wallet.core.model.WebhookTXStatus;
import com.hacks1ash.crypto.wallet.core.model.request.NewTransaction;
import com.hacks1ash.crypto.wallet.core.model.response.GetTransactionResponse;
import com.hacks1ash.crypto.wallet.core.storage.WalletRepository;
import com.hacks1ash.crypto.wallet.core.storage.WebhookRepository;
import com.hacks1ash.crypto.wallet.core.storage.WebhookSubscriptionRepository;
import com.hacks1ash.crypto.wallet.core.storage.document.Wallet;
import com.hacks1ash.crypto.wallet.core.storage.document.Webhook;
import com.hacks1ash.crypto.wallet.core.storage.document.WebhookSubscription;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
public class TransactionSubscriptionService implements TransactionListener {

  private WalletRepository walletRepository;

  private UTXOClientFactory utxoClientFactory;

  private WebhookSubscriptionRepository webhookSubscriptionRepository;

  private WalletManager walletManager;

  private WebhookRepository webhookRepository;


  @Override
  public void onTransaction(NewTransaction newTransaction) {
    CryptoCurrency cryptoCurrency = CryptoCurrency.cryptoCurrencyFromShortName(newTransaction.getCoin());
    UTXORPCClient rpcClient = utxoClientFactory.getClient(cryptoCurrency.getUtxoProvider());

    Set<Wallet> walletsByDepositAddresses = walletRepository.findAllByAddresses(newTransaction.getAddresses());
//    Set<Wallet> walletsByChangeAddresses = walletRepository.findAllByChangeAddresses(newTransaction.getAddresses());

    if (walletsByDepositAddresses == null || walletsByDepositAddresses.size() == 0) {
      return;
    }

    List<Wallet> wallets = new ArrayList<>(walletsByDepositAddresses);

    for (Wallet wallet : wallets) {
      try {
        WebhookSubscription webhookSubscription = webhookSubscriptionRepository.findByWalletId(wallet.getId()).orElseThrow(() -> new WalletException("wallet.not.subscribed", "Wallet not subscribed for webhooks with id -> " + wallet.getId(), 400));

        GetTrasactionResponse transaction = rpcClient.getTransaction(new GetTransactionRequest(cryptoCurrency.getUtxoProvider(), wallet.getNodeWalletNameAlias(), newTransaction.getTxid()), wallet.getCurrency().getNetworkParams());

        WebhookTXStatus txStatus;

        if (transaction.getConfirmations() == 0) {
          txStatus = WebhookTXStatus.MEMPOOL;
        } else if (transaction.getConfirmations() < webhookSubscription.getConfirmations()) {
          txStatus = WebhookTXStatus.PENDING;
        } else {
          txStatus = WebhookTXStatus.CONFIRMED;
        }


        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("x-wallet-id", wallet.getId());
        httpHeaders.add("x-provider", "OWS");
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        GetTransactionResponse webhookData = walletManager.getTransaction(wallet.getId(), newTransaction.getTxid());
        webhookData.setConfirmations(transaction.getConfirmations());
        HttpEntity<GetTransactionResponse> httpEntity = new HttpEntity<>(webhookData, httpHeaders);

        ResponseEntity<Void> response = restTemplate.postForEntity(webhookSubscription.getEndpoint(), httpEntity, Void.class);

        Webhook webhook = new Webhook(
          wallet.getId(),
          transaction.getTxid(),
          transaction.isReplaceable(),
          transaction.isReplaceable(),
          transaction.getConfirmations(),
          transaction.getBlockHeight(),
          txStatus,
          response.getStatusCode().is2xxSuccessful() ? WebhookStatus.DELIEVERED : WebhookStatus.FAILED,
          webhookData
        );
        log.info(webhookRepository.save(webhook).toString());
      } catch (WalletException ex) {
        if (ex.getErrorKey().equalsIgnoreCase("wallet.not.subscribed")) {
          log.warn(ex.getErrorMessage());
        }
        throw ex;
      }
    }
  }


  @Autowired
  public void setWalletRepository(WalletRepository walletRepository) {
    this.walletRepository = walletRepository;
  }

  @Autowired
  public void setBlockchainIntegrationFactory(UTXOClientFactory utxoClientFactory) {
    this.utxoClientFactory = utxoClientFactory;
  }

  @Autowired
  public void setWebhookSubscriptionRepository(WebhookSubscriptionRepository webhookSubscriptionRepository) {
    this.webhookSubscriptionRepository = webhookSubscriptionRepository;
  }

  @Autowired
  public void setWalletManager(WalletManager walletManager) {
    this.walletManager = walletManager;
  }

  @Autowired
  public void setWebhookRepository(WebhookRepository webhookRepository) {
    this.webhookRepository = webhookRepository;
  }
}

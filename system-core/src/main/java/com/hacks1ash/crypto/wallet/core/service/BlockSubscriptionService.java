package com.hacks1ash.crypto.wallet.core.service;

import com.hacks1ash.crypto.wallet.blockchain.UTXORPCClient;
import com.hacks1ash.crypto.wallet.core.BlockListener;
import com.hacks1ash.crypto.wallet.core.model.CryptoCurrency;
import com.hacks1ash.crypto.wallet.core.model.request.NewBlock;
import com.hacks1ash.crypto.wallet.core.storage.WalletRepository;
import com.hacks1ash.crypto.wallet.core.utils.BlockchainIntegrationFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class BlockSubscriptionService implements BlockListener {

  private WalletRepository walletRepository;

  private BlockchainIntegrationFactory blockchainIntegrationFactory;

  @PostConstruct
  void onStartup() {
  }

  @Override
  public void onBlock(NewBlock newBlock) {
    CryptoCurrency cryptoCurrency = CryptoCurrency.cryptoCurrencyFromShortName(newBlock.getCoin());
    UTXORPCClient rpcClient = blockchainIntegrationFactory.getRPCClient(cryptoCurrency);
  }

  @Autowired
  public void setWalletRepository(WalletRepository walletRepository) {
    this.walletRepository = walletRepository;
  }

  @Autowired
  public void setBlockchainIntegrationFactory(BlockchainIntegrationFactory blockchainIntegrationFactory) {
    this.blockchainIntegrationFactory = blockchainIntegrationFactory;
  }
}

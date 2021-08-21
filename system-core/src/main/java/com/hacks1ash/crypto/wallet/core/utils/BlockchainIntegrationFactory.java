package com.hacks1ash.crypto.wallet.core.utils;

import com.hacks1ash.crypto.wallet.blockchain.UTXORPCClient;
import com.hacks1ash.crypto.wallet.core.WalletException;
import com.hacks1ash.crypto.wallet.core.model.CryptoCurrency;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class BlockchainIntegrationFactory {

  private final UTXORPCClient bitcoinRPCClient;

  public UTXORPCClient getRPCClient(CryptoCurrency cryptoCurrency) {
    switch (cryptoCurrency) {
      case BITCOIN:
      case TEST_BITCOIN:
        return bitcoinRPCClient;
      default:
        throw new WalletException.CoinNotSupported(cryptoCurrency.getShortName());
    }
  }

  public BlockchainIntegrationFactory(@Qualifier("bitcoinRPCClient") UTXORPCClient bitcoinRPCClient) {
    this.bitcoinRPCClient = bitcoinRPCClient;
  }
}

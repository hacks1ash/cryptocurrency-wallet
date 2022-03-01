package com.hacks1ash.crypto.wallet.blockchain.model;

import com.hacks1ash.crypto.wallet.blockchain.factory.UTXOProvider;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bitcoinj.core.NetworkParameters;

import java.util.Arrays;
import java.util.List;

@Getter
@AllArgsConstructor
public enum NetworkParams {

  MAINNET(Arrays.asList(UTXOProvider.BITCOIN, UTXOProvider.LITECOIN)),
  TESTNET(Arrays.asList(UTXOProvider.TEST_BITCOIN, UTXOProvider.TEST_LITECOIN)),
  LOCAL(Arrays.asList(UTXOProvider.LOCAL_BITCOIN, UTXOProvider.LOCAL_LITECOIN));

  private final List<UTXOProvider> utxoProviderList;

}

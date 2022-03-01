package com.hacks1ash.crypto.wallet.core.model;

import com.hacks1ash.crypto.wallet.blockchain.factory.UTXOProvider;
import com.hacks1ash.crypto.wallet.blockchain.model.NetworkParams;
import com.hacks1ash.crypto.wallet.core.WalletException;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CryptoCurrency {

  BITCOIN("btc", NetworkParams.MAINNET, FeeUnit.SATOSHI, 8, UTXOProvider.BITCOIN),
  TEST_BITCOIN("tbtc", NetworkParams.TESTNET, FeeUnit.SATOSHI, 8, UTXOProvider.BITCOIN),
  LOCAL_BITCOIN("lbtc", NetworkParams.LOCAL, FeeUnit.SATOSHI, 8, UTXOProvider.BITCOIN),
  LITECOIN("ltc", NetworkParams.MAINNET, FeeUnit.LITOSHI, 8, UTXOProvider.LITECOIN),
  TEST_LITECOIN("tltc", NetworkParams.TESTNET, FeeUnit.LITOSHI, 8, UTXOProvider.LITECOIN),
  LOCAL_LITECOIN("lltc", NetworkParams.LOCAL, FeeUnit.LITOSHI, 8, UTXOProvider.LITECOIN);

  private final String shortName;
  private final NetworkParams networkParams;
  private final FeeUnit feeUnit;
  private final int decimalPoints;
  private final UTXOProvider utxoProvider;


  public static CryptoCurrency cryptoCurrencyFromShortName(String shortName) {
    for (CryptoCurrency value : CryptoCurrency.values()) {
      if (shortName.toLowerCase().equals(value.getShortName())) {
        return value;
      }
    }

    throw new WalletException.CoinNotSupported(shortName);
  }
}

package com.hacks1ash.crypto.wallet.core.model;

import com.hacks1ash.crypto.wallet.core.WalletException;
import org.bitcoinj.core.NetworkParameters;

public enum CryptoCurrency {

  BITCOIN("btc", NetworkParameters.fromID(NetworkParameters.ID_MAINNET), FeeUnit.SATOSHI, 8),
  TEST_BITCOIN("tbtc", NetworkParameters.fromID(NetworkParameters.ID_TESTNET), FeeUnit.SATOSHI, 8);

  private final String shortName;
  private final NetworkParameters networkParameters;
  private final FeeUnit feeUnit;
  private final int decimalPoints;

  CryptoCurrency(String shortName, NetworkParameters networkParameters, FeeUnit feeUnit, int decimalPoints) {
    this.shortName = shortName;
    this.networkParameters = networkParameters;
    this.feeUnit = feeUnit;
    this.decimalPoints = decimalPoints;
  }

  public static CryptoCurrency cryptoCurrencyFromShortName(String shortName) {
    for (CryptoCurrency value : CryptoCurrency.values()) {
      if (shortName.toLowerCase().equals(value.getShortName())) {
        return value;
      }
    }

    throw new WalletException.CoinNotSupported(shortName);
  }

  public String getShortName() {
    return shortName;
  }

  public NetworkParameters getNetworkParameters() {
    return networkParameters;
  }

  public FeeUnit getFeeUnit() {
    return feeUnit;
  }

  public int getDecimalPoints() {
    return decimalPoints;
  }
}

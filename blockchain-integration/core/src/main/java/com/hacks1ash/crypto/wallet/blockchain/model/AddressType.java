package com.hacks1ash.crypto.wallet.blockchain.model;

import com.hacks1ash.crypto.wallet.blockchain.factory.UTXOProvider;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;

@Getter
@AllArgsConstructor
public enum AddressType {

  P2PKH(Arrays.asList(UTXOProvider.BITCOIN, UTXOProvider.TEST_BITCOIN, UTXOProvider.LOCAL_BITCOIN)),
  P2SH(Arrays.asList(UTXOProvider.BITCOIN, UTXOProvider.TEST_BITCOIN, UTXOProvider.LOCAL_BITCOIN)),
  BECH_32(Arrays.asList(UTXOProvider.BITCOIN, UTXOProvider.TEST_BITCOIN, UTXOProvider.LOCAL_BITCOIN)),
  CASH_ADDRESS(Arrays.asList()),
  DEFAULT(Arrays.asList(UTXOProvider.BITCOIN, UTXOProvider.TEST_BITCOIN, UTXOProvider.LOCAL_BITCOIN));

  private final List<UTXOProvider> supportedCoinList;
}

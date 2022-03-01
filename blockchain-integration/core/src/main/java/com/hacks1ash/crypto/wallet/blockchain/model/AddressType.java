package com.hacks1ash.crypto.wallet.blockchain.model;

import com.hacks1ash.crypto.wallet.blockchain.factory.UTXOProvider;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;

@Getter
@AllArgsConstructor
public enum AddressType {

  P2PKH(Arrays.asList(UTXOProvider.BITCOIN)),
  P2SH(Arrays.asList(UTXOProvider.BITCOIN)),
  BECH_32(Arrays.asList(UTXOProvider.BITCOIN)),
  CASH_ADDRESS(Arrays.asList(UTXOProvider.BITCOIN_CASH)),
  DEFAULT(Arrays.asList(UTXOProvider.BITCOIN));

  private final List<UTXOProvider> supportedCoinList;
}

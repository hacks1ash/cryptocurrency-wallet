package com.hacks1ash.crypto.wallet.blockchain.factory;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UTXOProvider {
  BITCOIN,
  LITECOIN,
  BITCOIN_CASH;

}

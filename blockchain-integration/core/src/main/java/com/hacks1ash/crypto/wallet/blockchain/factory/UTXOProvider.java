package com.hacks1ash.crypto.wallet.blockchain.factory;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UTXOProvider {

  BITCOIN,
  TEST_BITCOIN,
  LOCAL_BITCOIN,

  LITECOIN,
  TEST_LITECOIN,
  LOCAL_LITECOIN,

  BITCOIN_CASH,
  TEST_BITCOIN_CASH,
  LOCAL_BITCOIN_CASH;

}

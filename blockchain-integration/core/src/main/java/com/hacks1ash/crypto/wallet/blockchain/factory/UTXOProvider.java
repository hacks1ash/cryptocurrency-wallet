package com.hacks1ash.crypto.wallet.blockchain.factory;

import com.hacks1ash.crypto.wallet.blockchain.litecoin.config.LitecoinMainNetParams;
import com.hacks1ash.crypto.wallet.blockchain.litecoin.config.LitecoinRegTestParams;
import com.hacks1ash.crypto.wallet.blockchain.litecoin.config.LitecoinTestNet3Params;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.params.MainNetParams;
import org.bitcoinj.params.RegTestParams;
import org.bitcoinj.params.TestNet3Params;

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

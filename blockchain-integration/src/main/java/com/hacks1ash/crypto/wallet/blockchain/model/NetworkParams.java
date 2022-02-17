package com.hacks1ash.crypto.wallet.blockchain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bitcoinj.core.NetworkParameters;

@Getter
@AllArgsConstructor
public enum NetworkParams {

  MAINNET(NetworkParameters.fromID(NetworkParameters.ID_MAINNET)),
  TESTNET(NetworkParameters.fromID(NetworkParameters.ID_TESTNET)),
  LOCAL(NetworkParameters.fromID(NetworkParameters.ID_REGTEST));

  private final NetworkParameters networkParameters;

}

package com.hacks1ash.crypto.wallet.blockchain.bitcoin;

import com.hacks1ash.crypto.wallet.blockchain.AbstractRPCClient;
import com.hacks1ash.crypto.wallet.blockchain.config.UTXOConfigProperties;
import com.hacks1ash.crypto.wallet.blockchain.factory.UTXOProvider;
import com.hacks1ash.crypto.wallet.blockchain.factory.UTXOQualifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@UTXOQualifier(providers = {UTXOProvider.BITCOIN})
public class BitcoinRPCClient extends AbstractRPCClient {

  public BitcoinRPCClient(@Autowired UTXOConfigProperties properties) {
    super(properties);
  }

}

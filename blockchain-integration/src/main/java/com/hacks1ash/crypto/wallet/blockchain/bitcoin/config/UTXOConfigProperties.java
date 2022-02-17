package com.hacks1ash.crypto.wallet.blockchain.bitcoin.config;

import com.hacks1ash.crypto.wallet.blockchain.factory.UTXOProvider;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

@Data
@Configuration
@ConfigurationProperties(prefix = "utxo")
public class UTXOConfigProperties {

  private EnumMap<UTXOProvider, NodeConfig> nodes = new EnumMap<>(UTXOProvider.class);

  @Data
  public static class NodeConfig {

    private String scheme;

    private String host;

    private int port;

    private String username;

    private String password;

  }

}

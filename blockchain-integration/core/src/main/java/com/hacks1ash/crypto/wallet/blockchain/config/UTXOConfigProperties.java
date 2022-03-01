package com.hacks1ash.crypto.wallet.blockchain.config;

import com.hacks1ash.crypto.wallet.blockchain.factory.UTXOProvider;
import com.hacks1ash.crypto.wallet.blockchain.model.NetworkParams;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.EnumMap;

@Data
@Configuration
@ConfigurationProperties(prefix = "utxo")
public class UTXOConfigProperties {

  private EnumMap<UTXOProvider, NodeConfig> nodes = new EnumMap<>(UTXOProvider.class);

  @Data
  public static class NodeConfig {

    private String scheme;

    private String host;

    private Integer port;

    private String username;

    private String password;

    private NetworkParams network;

    private boolean enabled;

  }

}

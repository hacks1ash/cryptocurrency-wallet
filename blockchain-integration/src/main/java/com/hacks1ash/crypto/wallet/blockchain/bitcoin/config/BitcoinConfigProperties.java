package com.hacks1ash.crypto.wallet.blockchain.bitcoin.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "bitcoin")
public class BitcoinConfigProperties {

  public String rpcScheme;

  public String rpcHost;

  public int rpcPort;

  public String rpcUsername;

  public String rpcPassword;

}

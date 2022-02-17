package com.hacks1ash.crypto.wallet;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(scanBasePackages = {"com.hacks1ash.crypto.wallet"})
@EnableMongoRepositories(basePackages = {"com.hacks1ash.crypto.wallet"})
@EntityScan(basePackages = {"com.hacks1ash.crypto.wallet"})
@EnableScheduling
@EnableAsync
@EnableKafka
@EnableConfigurationProperties
@ConfigurationPropertiesScan("com.hacks1ash.crypto.wallet")
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class RestApplication extends SpringBootServletInitializer {

  @Override
  protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
    return builder.sources(RestApplication.class);
  }

  public static void main(String[] args) {
    SpringApplication.run(RestApplication.class, args);
  }

}

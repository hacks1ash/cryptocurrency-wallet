package com.hacks1ash.crypto.wallet.blockchain.factory;

import com.hacks1ash.crypto.wallet.blockchain.UTXOAddressManager;
import com.hacks1ash.crypto.wallet.blockchain.UTXORPCClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class UTXOClientFactory {

  private final ApplicationContext applicationContext;

  @Autowired
  public UTXOClientFactory(ApplicationContext applicationContext) {
    this.applicationContext = applicationContext;
  }

  public UTXORPCClient getClient(UTXOProvider provider) {
    Map<String, Object> beansWithAnnotation = applicationContext.getBeansWithAnnotation(UTXOQualifier.class);
    for (Object value : beansWithAnnotation.values()) {
      if (value instanceof UTXORPCClient) {
        UTXOQualifier annotation = value.getClass().getAnnotation(UTXOQualifier.class);
        UTXOProvider[] providers = annotation.providers();
        for (UTXOProvider utxoProvider : providers) {
          if (utxoProvider == provider) {
            return (UTXORPCClient) value;
          }
        }
      }
    }
    // TODO throw exception
    return null;
  }

  public UTXOAddressManager getAddressManager(UTXOProvider provider) {
    Map<String, Object> beansWithAnnotation = applicationContext.getBeansWithAnnotation(AddressQualifier.class);
    for (Object value : beansWithAnnotation.values()) {
      if (value instanceof UTXOAddressManager) {
        AddressQualifier annotation = value.getClass().getAnnotation(AddressQualifier.class);
        UTXOProvider[] providers = annotation.providers();
        for (UTXOProvider utxoProvider : providers) {
          if (utxoProvider == provider) {
            return (UTXOAddressManager) value;
          }
        }
      }
    }
    // TODO throw exception
    return null;
  }

}

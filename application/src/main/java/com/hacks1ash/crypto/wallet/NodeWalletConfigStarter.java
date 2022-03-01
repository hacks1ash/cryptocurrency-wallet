package com.hacks1ash.crypto.wallet;

import com.hacks1ash.crypto.wallet.blockchain.GenericRpcException;
import com.hacks1ash.crypto.wallet.blockchain.UTXOAddressManager;
import com.hacks1ash.crypto.wallet.blockchain.UTXORPCClient;
import com.hacks1ash.crypto.wallet.blockchain.factory.UTXOClientFactory;
import com.hacks1ash.crypto.wallet.blockchain.model.AddressType;
import com.hacks1ash.crypto.wallet.blockchain.model.AddressWithPrivate;
import com.hacks1ash.crypto.wallet.blockchain.model.request.CreateWalletRequest;
import com.hacks1ash.crypto.wallet.blockchain.model.request.ImportMultiRequest;
import com.hacks1ash.crypto.wallet.core.model.Address;
import com.hacks1ash.crypto.wallet.core.storage.WalletRepository;
import com.hacks1ash.crypto.wallet.core.storage.document.Wallet;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.time.Instant;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

@Slf4j
@Configuration
public class NodeWalletConfigStarter {

  private final WalletRepository walletRepository;
  private final UTXOClientFactory utxoClientFactory;

  @Autowired
  public NodeWalletConfigStarter(WalletRepository walletRepository, UTXOClientFactory utxoClientFactory) {
    this.walletRepository = walletRepository;
    this.utxoClientFactory = utxoClientFactory;
  }

  @PostConstruct
  public void loadOrCreateWalletsInNode() {
    List<Wallet> wallets = walletRepository.findAll();
    for (Wallet wallet : wallets) {
      UTXORPCClient client = utxoClientFactory.getClient(wallet.getCurrency().getUtxoProvider());
      UTXOAddressManager addressManager = utxoClientFactory.getAddressManager(wallet.getCurrency().getUtxoProvider());
      boolean needsRescan = false;
      try {
        client.createWallet(
          new CreateWalletRequest(
            wallet.getCurrency().getUtxoProvider(),
            wallet.getNodeWalletNameAlias(),
            true
          ),
          wallet.getCurrency().getNetworkParams()
        );

        AddressWithPrivate changeAddress = addressManager.createAddress(wallet.getHdSeed(), AddressType.BECH_32, wallet.getCurrency().getNetworkParams(), 0, wallet.getCreationTimestamp());

        client.importMulti(
          wallet.getCurrency().getUtxoProvider(),
          wallet.getNodeWalletNameAlias(),
          Collections.singletonList(
            new ImportMultiRequest(
              new HashMap<String, String>() {{
                put("address", changeAddress.getAddress());
              }},
              changeAddress.getRedeemScript(),
              null,
              Collections.singletonList(changeAddress.getPrivateKey()),
              true,
              null
            )
          ),
          false,
          wallet.getCurrency().getNetworkParams()
        );

        needsRescan = true;
      } catch (GenericRpcException ex) {
        log.error(ex.getErrorMessage());
      }
      int addressIndex = 1;
      List<Address> addresses = wallet.getAddresses();

      for (Address address : addresses) {
        AddressWithPrivate newAddress = addressManager.createAddress(wallet.getHdSeed(), AddressType.valueOf(address.getType().name()), wallet.getCurrency().getNetworkParams(), addressIndex, Instant.now().getEpochSecond());

        try {
          client.importMulti(
            wallet.getCurrency().getUtxoProvider(),
            wallet.getNodeWalletNameAlias(),
            Collections.singletonList(
              new ImportMultiRequest(
                new HashMap<String, String>() {{
                  put("address", newAddress.getAddress());
                }},
                newAddress.getRedeemScript(),
                null,
                Collections.singletonList(newAddress.getPrivateKey()),
                false,
                address.getName()
              )
            ),
            false,
            wallet.getCurrency().getNetworkParams()
          );
          addressIndex++;
          needsRescan = true;
        } catch (GenericRpcException ex) {
          log.error(ex.getMessage());
        }
      }

      if (needsRescan) {
        // TODO
      }
    }
  }

}

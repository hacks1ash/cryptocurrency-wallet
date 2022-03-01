package com.hacks1ash.crypto.wallet.blockchain.bch;

import com.hacks1ash.crypto.wallet.blockchain.UTXOAddressManager;
import com.hacks1ash.crypto.wallet.blockchain.factory.AddressQualifier;
import com.hacks1ash.crypto.wallet.blockchain.factory.UTXOProvider;
import com.hacks1ash.crypto.wallet.blockchain.model.AddressType;
import com.hacks1ash.crypto.wallet.blockchain.model.AddressWithPrivate;
import com.hacks1ash.crypto.wallet.blockchain.model.NetworkParams;
import org.bitcoinj.core.Address;
import org.bitcoinj.core.CashAddress;
import org.bitcoinj.core.ECKey;
import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.crypto.DeterministicKey;
import org.bitcoinj.crypto.HDKeyDerivation;
import org.bitcoinj.wallet.DeterministicSeed;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
@AddressQualifier(providers = {UTXOProvider.BITCOIN_CASH})
public class BitcoinCashAddressManagerBean implements UTXOAddressManager {

  private static final Map<NetworkParams, NetworkParameters> networkParamsUTXOProviderMap = new HashMap<>();

  static {
    networkParamsUTXOProviderMap.put(NetworkParams.MAINNET, NetworkParameters.fromID(NetworkParameters.ID_MAINNET));
    networkParamsUTXOProviderMap.put(NetworkParams.TESTNET, NetworkParameters.fromID(NetworkParameters.ID_TESTNET));
    networkParamsUTXOProviderMap.put(NetworkParams.LOCAL, NetworkParameters.fromID(NetworkParameters.ID_REGTEST));
  }

  @Override
  public AddressWithPrivate createAddress(List<String> hdSeed, AddressType addressType, NetworkParams networkParameters, int addressIndex, long creationTimestamp) {
    DeterministicSeed masterSeed = new DeterministicSeed(hdSeed, null, "", creationTimestamp);
    byte[] seed = Objects.requireNonNull(masterSeed.getSeedBytes());
    DeterministicKey masterPrivateKey = HDKeyDerivation.createMasterPrivateKey(seed);

    DeterministicKey deterministicKey = HDKeyDerivation.deriveChildKey(masterPrivateKey, addressIndex);
    ECKey ecKey = ECKey.fromPrivate(deterministicKey.getPrivKey());

    NetworkParameters parameters = networkParamsUTXOProviderMap.get(networkParameters);

    String privateKeyAsWiF = deterministicKey.getPrivateKeyAsWiF(parameters);

    switch (addressType) {
      case P2PKH:
        return new AddressWithPrivate(Address.fromKey(parameters, ecKey).toString(), privateKeyAsWiF);
      case BECH_32:
      case P2SH:
      case CASH_ADDRESS:
      case DEFAULT:
      default:
        Address address = CashAddress.fromKey(parameters, ecKey);
        return new AddressWithPrivate(address.toString(), privateKeyAsWiF);
    }
  }

}

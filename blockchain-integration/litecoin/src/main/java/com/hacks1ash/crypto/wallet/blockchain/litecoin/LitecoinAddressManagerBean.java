package com.hacks1ash.crypto.wallet.blockchain.litecoin;

import com.hacks1ash.crypto.wallet.blockchain.UTXOAddressManager;
import com.hacks1ash.crypto.wallet.blockchain.factory.AddressQualifier;
import com.hacks1ash.crypto.wallet.blockchain.factory.UTXOProvider;
import com.hacks1ash.crypto.wallet.blockchain.litecoin.config.LitecoinMainNetParams;
import com.hacks1ash.crypto.wallet.blockchain.litecoin.config.LitecoinRegTestParams;
import com.hacks1ash.crypto.wallet.blockchain.litecoin.config.LitecoinTestNet3Params;
import com.hacks1ash.crypto.wallet.blockchain.model.AddressType;
import com.hacks1ash.crypto.wallet.blockchain.model.AddressWithPrivate;
import com.hacks1ash.crypto.wallet.blockchain.model.NetworkParams;
import org.bitcoinj.core.ECKey;
import org.bitcoinj.core.LegacyAddress;
import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.crypto.DeterministicKey;
import org.bitcoinj.crypto.HDKeyDerivation;
import org.bitcoinj.script.Script;
import org.bitcoinj.script.ScriptBuilder;
import org.bitcoinj.script.ScriptPattern;
import org.bitcoinj.wallet.DeterministicSeed;
import org.springframework.stereotype.Service;

import javax.xml.bind.DatatypeConverter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;


@Service
@AddressQualifier(providers = {UTXOProvider.LITECOIN})
public class LitecoinAddressManagerBean implements UTXOAddressManager {

  private static final Map<NetworkParams, NetworkParameters> networkParamsUTXOProviderMap = new HashMap<>();

  static {
    networkParamsUTXOProviderMap.put(NetworkParams.MAINNET, new LitecoinMainNetParams());
    networkParamsUTXOProviderMap.put(NetworkParams.TESTNET, new LitecoinTestNet3Params());
    networkParamsUTXOProviderMap.put(NetworkParams.LOCAL, new LitecoinRegTestParams());
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
        return new AddressWithPrivate(LegacyAddress.fromKey(parameters, ecKey).toString(), privateKeyAsWiF);
      case BECH_32:
      case DEFAULT:
      case P2SH:
      default:
        Script redeemScript = ScriptBuilder.createP2WPKHOutputScript(ecKey);
        Script script = ScriptBuilder.createP2SHOutputScript(redeemScript);
        byte[] scriptHash = ScriptPattern.extractHashFromP2SH(script);
        return new AddressWithPrivate(LegacyAddress.fromScriptHash(parameters, scriptHash).toString(), privateKeyAsWiF, DatatypeConverter.printHexBinary(redeemScript.getProgram()));
    }
  }
}

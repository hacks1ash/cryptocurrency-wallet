package com.hacks1ash.crypto.wallet.blockchain;

import com.hacks1ash.crypto.wallet.blockchain.model.AddressType;
import com.hacks1ash.crypto.wallet.blockchain.model.AddressWithPrivate;
import com.hacks1ash.crypto.wallet.blockchain.model.NetworkParams;

import java.util.List;

public interface UTXOAddressManager {

  AddressWithPrivate createAddress(List<String> hdSeed, AddressType addressType, NetworkParams networkParameters, int addressIndex, long creationTimestamp);

}

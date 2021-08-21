package com.hacks1ash.crypto.wallet.core.storage;

import com.hacks1ash.crypto.wallet.core.storage.document.Wallet;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WalletRepository extends MongoRepository<Wallet, String> {
}

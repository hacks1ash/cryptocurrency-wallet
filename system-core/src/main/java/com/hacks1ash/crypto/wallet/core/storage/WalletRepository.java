package com.hacks1ash.crypto.wallet.core.storage;

import com.hacks1ash.crypto.wallet.core.storage.document.Wallet;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Set;

@Repository
public interface WalletRepository extends MongoRepository<Wallet, String> {

  @Query("{ 'addresses.address': { $in : ?0 }}")
  Set<Wallet> findAllByAddresses(List<String> addresses);

  @Query("{ 'changeAddress': { $in : ?0 }}")
  Set<Wallet> findAllByChangeAddresses(List<String> changeAddresses);


}

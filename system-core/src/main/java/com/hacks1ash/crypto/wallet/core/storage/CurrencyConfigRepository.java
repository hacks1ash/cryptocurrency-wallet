package com.hacks1ash.crypto.wallet.core.storage;


import com.hacks1ash.crypto.wallet.core.model.CryptoCurrency;
import com.hacks1ash.crypto.wallet.core.storage.document.CurrencyConfig;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CurrencyConfigRepository extends MongoRepository<CurrencyConfig, String> {

  Optional<CurrencyConfig> findByCurrency(CryptoCurrency currency);

}

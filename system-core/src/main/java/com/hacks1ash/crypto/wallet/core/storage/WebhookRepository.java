package com.hacks1ash.crypto.wallet.core.storage;

import com.hacks1ash.crypto.wallet.core.storage.document.Webhook;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WebhookRepository extends MongoRepository<Webhook, String> {

  List<Webhook> findAllByWalletIdAndTxId(String walletId, String txId);

}

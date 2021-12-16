package com.hacks1ash.crypto.wallet.core.storage;

import com.hacks1ash.crypto.wallet.core.storage.document.WebhookSubscription;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WebhookSubscriptionRepository extends MongoRepository<WebhookSubscription, String> {
}

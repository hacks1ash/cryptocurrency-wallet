package com.hacks1ash.crypto.wallet.core;

import com.hacks1ash.crypto.wallet.core.model.request.NewTransaction;

public interface TransactionListener {

  void onTransaction(NewTransaction newTransaction);

}

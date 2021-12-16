package com.hacks1ash.crypto.wallet.core;

import com.hacks1ash.crypto.wallet.core.model.request.NewBlock;

public interface BlockListener {

  void onBlock(NewBlock newBlock);

}

package com.hacks1ash.crypto.wallet.rest;

import com.hacks1ash.crypto.wallet.core.BlockListener;
import com.hacks1ash.crypto.wallet.core.TransactionListener;
import com.hacks1ash.crypto.wallet.core.model.request.NewBlock;
import com.hacks1ash.crypto.wallet.core.model.request.NewTransaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "node/")
public class BlockchainController {

  private BlockListener blockListener;

  private TransactionListener transactionListener;

  @RequestMapping(value = "block", method = RequestMethod.POST)
  public void newBlock(@RequestBody NewBlock newBlock) {
    blockListener.onBlock(newBlock);
  }

  @RequestMapping(value = "transaction", method = RequestMethod.POST)
  public void newTransaction(@RequestBody NewTransaction newTransaction) {
    transactionListener.onTransaction(newTransaction);
  }

  @Autowired
  public void setBlockListener(BlockListener blockListener) {
    this.blockListener = blockListener;
  }

  @Autowired
  public void setTransactionListener(TransactionListener transactionListener) {
    this.transactionListener = transactionListener;
  }
}

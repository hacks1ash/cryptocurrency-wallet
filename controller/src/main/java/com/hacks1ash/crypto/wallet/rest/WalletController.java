package com.hacks1ash.crypto.wallet.rest;

import com.hacks1ash.crypto.wallet.core.WalletManager;
import com.hacks1ash.crypto.wallet.core.model.request.AddressCreationRequest;
import com.hacks1ash.crypto.wallet.core.model.request.TransactionRequest;
import com.hacks1ash.crypto.wallet.core.model.request.WalletCreationRequest;
import com.hacks1ash.crypto.wallet.core.model.response.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.List;

@RestController
@RequestMapping(value = "wallet")
public class WalletController {

  private WalletManager walletManager;

  @PostMapping
  public WalletResponse createWallet(@RequestBody WalletCreationRequest request) {
    return walletManager.createWallet(request);
  }

  @GetMapping
  public List<WalletResponse> listWallets() {
    return walletManager.listWallets();
  }

  @RequestMapping(value = "/{walletId}/balance", method = RequestMethod.GET)
  public BigInteger getBalance(@PathVariable String walletId) {
    return walletManager.getBalance(walletId);
  }

  @RequestMapping(value = "/{walletId}/address", method = RequestMethod.POST)
  public AddressResponse createAddress(@PathVariable String walletId, @RequestBody AddressCreationRequest request) {
    return walletManager.createAddress(walletId, request);
  }

  @RequestMapping(value = "/{walletId}/address", method = RequestMethod.GET)
  public List<AddressResponse> getAddresses(@PathVariable String walletId) {
    return walletManager.getAddresses(walletId);
  }

  @RequestMapping(value = "/{walletId}/estimateFee", method = RequestMethod.PUT)
  public EstimateFeeResponse estimateFee(@PathVariable String walletId, @RequestBody TransactionRequest request) {
    return walletManager.estimateFee(walletId, request);
  }

  @RequestMapping(value = "/{walletId}/transaction", method = RequestMethod.POST)
  public SendTransactionResponse sendTransaction(@PathVariable String walletId, @RequestBody TransactionRequest request) {
    return walletManager.sendTransaction(walletId, request);
  }

  @RequestMapping(value = "/{walletId}/transaction/{txId}", method = RequestMethod.GET)
  public GetTransactionResponse getTransaction(@PathVariable String walletId, @PathVariable String txId) {
    return walletManager.getTransaction(walletId, txId);
  }

  @Autowired
  public void setWalletManager(WalletManager walletManager) {
    this.walletManager = walletManager;
  }
}

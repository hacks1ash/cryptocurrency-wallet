package com.hacks1ash.crypto.wallet.blockchain.bitcoin;

import com.hacks1ash.crypto.wallet.blockchain.RPCClientImpl;
import com.hacks1ash.crypto.wallet.blockchain.UTXORPCClient;
import com.hacks1ash.crypto.wallet.blockchain.bitcoin.config.BitcoinConfigProperties;
import com.hacks1ash.crypto.wallet.blockchain.bitcoin.model.request.CreateWalletRequest;
import com.hacks1ash.crypto.wallet.blockchain.bitcoin.model.request.EstimateSmartFeeRequest;
import com.hacks1ash.crypto.wallet.blockchain.bitcoin.model.request.GetBalanceRequest;
import com.hacks1ash.crypto.wallet.blockchain.bitcoin.model.request.ImportPrivateKeyRequest;
import com.hacks1ash.crypto.wallet.blockchain.bitcoin.model.response.CreateWalletResponse;
import com.hacks1ash.crypto.wallet.blockchain.bitcoin.model.response.EstimateSmartFeeResponse;
import com.hacks1ash.crypto.wallet.blockchain.bitcoin.model.response.impl.CreateWalletResponseWrapper;
import com.hacks1ash.crypto.wallet.blockchain.bitcoin.model.response.impl.EstimateSmartFeeResponseWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.util.Map;

@Service("bitcoinRPCClient")
public class BitcoinRPCClient extends RPCClientImpl implements UTXORPCClient {

  public BitcoinRPCClient(@Autowired BitcoinConfigProperties properties) throws MalformedURLException {
    super(properties.getRpcScheme() + "://" + properties.getRpcUsername() + ":" + properties.getRpcPassword() + "@" + properties.getRpcHost() + ":" + properties.getRpcPort());
  }

  @Override
  @SuppressWarnings({"unchecked", "unsafe"})
  public CreateWalletResponse createWallet(CreateWalletRequest request) {
    return new CreateWalletResponseWrapper(
      (Map<String, ?>) query(
        null,
        "createwallet",
        request.getName(),
        request.isDisablePrivateKeys(),
        request.isBlank(),
        request.getPassphrase(),
        request.isAvoidReuse(),
        request.isDescriptors(),
        request.isLoadOnStartup()
      )
    );
  }

  @Override
  public BigDecimal getBalance(GetBalanceRequest request) {
    return (BigDecimal) query(
      request.getWalletId(),
      "getbalance",
      "*",
      request.getMinConf(),
      request.isIncludeWatchonly(),
      request.isAvoidReuse()
    );
  }

  @Override
  @SuppressWarnings({"unchecked", "unsafe"})
  public EstimateSmartFeeResponse estimateSmartFee(EstimateSmartFeeRequest request) {
    return new EstimateSmartFeeResponseWrapper(
      (Map<String, ?>) query(
        null,
        "estimatesmartfee",
        request.getConfTargetBlock(),
        request.getEstimateMode().name()
      )
    );
  }

  @Override
  public void importPrivateKey(ImportPrivateKeyRequest request) {
    queryForStream(
      request.getWalletId(),
      "importprivkey",
      request.getPrivateKey(),
      request.getLabel(),
      request.isRescan()
    );
  }

}

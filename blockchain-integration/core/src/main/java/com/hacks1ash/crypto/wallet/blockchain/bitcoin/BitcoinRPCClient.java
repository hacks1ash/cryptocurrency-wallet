package com.hacks1ash.crypto.wallet.blockchain.bitcoin;

import co.elastic.apm.api.CaptureSpan;
import com.hacks1ash.crypto.wallet.blockchain.RPCClientImpl;
import com.hacks1ash.crypto.wallet.blockchain.UTXORPCClient;
import com.hacks1ash.crypto.wallet.blockchain.config.UTXOConfigProperties;
import com.hacks1ash.crypto.wallet.blockchain.bitcoin.model.BitcoinRawTxBuilder;
import com.hacks1ash.crypto.wallet.blockchain.bitcoin.model.request.*;
import com.hacks1ash.crypto.wallet.blockchain.bitcoin.model.response.*;
import com.hacks1ash.crypto.wallet.blockchain.bitcoin.model.response.impl.*;
import com.hacks1ash.crypto.wallet.blockchain.factory.UTXOProvider;
import com.hacks1ash.crypto.wallet.blockchain.factory.UTXOQualifier;
import com.hacks1ash.crypto.wallet.blockchain.utils.HexCoder;
import com.hacks1ash.crypto.wallet.blockchain.utils.ListMapWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@UTXOQualifier(providers = {UTXOProvider.BITCOIN, UTXOProvider.LOCAL_BITCOIN, UTXOProvider.TEST_BITCOIN, UTXOProvider.LITECOIN, UTXOProvider.TEST_LITECOIN, UTXOProvider.LOCAL_LITECOIN})
public class BitcoinRPCClient extends RPCClientImpl implements UTXORPCClient {

  public BitcoinRPCClient(@Autowired UTXOConfigProperties properties) {
    super(properties);
  }

  @Override
  @SuppressWarnings({"unchecked", "unsafe"})
  @CaptureSpan
  public CreateWalletResponse createWallet(CreateWalletRequest request) {
    return new CreateWalletResponseWrapper(
      (Map<String, ?>) query(
        request.getUtxoProvider(),
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
  @CaptureSpan
  public BigDecimal getBalance(GetBalanceRequest request) {
    return (BigDecimal) query(
      request.getUtxoProvider(),
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
  @CaptureSpan
  public EstimateSmartFeeResponse estimateSmartFee(EstimateSmartFeeRequest request) {
    return new EstimateSmartFeeResponseWrapper(
      (Map<String, ?>) query(
        request.getUtxoProvider(),
        null,
        "estimatesmartfee",
        request.getConfTargetBlock(),
        request.getEstimateMode().name()
      )
    );
  }

  @Override
  @CaptureSpan
  public void importPrivateKey(ImportPrivateKeyRequest request) {
    queryForStream(
      request.getUtxoProvider(),
      request.getWalletId(),
      "importprivkey",
      request.getPrivateKey(),
      request.getLabel(),
      request.isRescan()
    );
  }

  @Override
  @CaptureSpan
  public String createRawTransaction(UTXOProvider utxoProvider, String walletId, ArrayList<BitcoinRawTxBuilder.TxInput> inputs, List<BitcoinRawTxBuilder.TxOutput> outputs) {
    List<Map<String, ?>> pInputs = new ArrayList<>();

    for (final BitcoinRawTxBuilder.TxInput txInput : inputs) {
      pInputs.add(new LinkedHashMap<>() {
        {
          put("txid", txInput.txid());
          put("vout", txInput.vout());
        }
      });
    }

    Map<String, Object> pOutputs = new LinkedHashMap<>();

    for (BitcoinRawTxBuilder.TxOutput txOutput : outputs) {
      pOutputs.put(txOutput.address(), txOutput.amount());
      if (txOutput.data() != null) {
        String hex = HexCoder.encode(txOutput.data());
        pOutputs.put("data", hex);
      }
    }

    return (String) query(utxoProvider, walletId, "createrawtransaction", pInputs, pOutputs);
  }

  @Override
  @SuppressWarnings({"unchecked", "unsafe"})
  @CaptureSpan
  public FundRawTransactionResponse fundRawTransaction(FundRawTransactionRequest request) {
    LinkedHashMap<String, Object> options = new LinkedHashMap<>() {
      {
        if (request.getAddInputs() != null) {
          put("add_inputs", request.getAddInputs());
        }
        if (request.getChangeAddress() != null) {
          put("changeAddress", request.getChangeAddress());
        }
        if (request.getChangePosition() != null) {
          put("changePosition", request.getChangePosition());
        }
        if (request.getChangeType() != null) {
          put("change_type", request.getChangeType());
        }
        if (request.getIncludeWatching() != null) {
          put("includeWatching", request.getIncludeWatching());
        }
        if (request.getLockUnspents() != null) {
          put("lockUnspents", request.getLockUnspents());
        }
        if (request.getFeeRateBySatoshi() != null) {
          put("fee_rate", request.getFeeRateBySatoshi());
        }
        if (request.getFeeRateByBTC() != null) {
          put("feeRate", request.getFeeRateByBTC());
        }
        if (request.getSubtractFeeFromOutputs() != null) {
          put("subtractFeeFromOutputs", request.getSubtractFeeFromOutputs());
        }
        if (request.getReplaceable() != null) {
          put("replaceable", request.getReplaceable());
        }
        if (request.getConfTarget() != null) {
          put("conf_target", request.getConfTarget());
        }
        if (request.getEstimateMode() != null) {
          put("estimate_mode", request.getEstimateMode().name());
        }
      }
    };

    return new FundRawTransactionResponseWrapper(
      (Map<String, ?>) query(
        request.getUtxoProvider(),
        request.getWalletId(),
        "fundrawtransaction",
        request.getTxId(),
        options,
        request.isWitness()
      )
    );
  }

  @Override
  @SuppressWarnings({"unchecked", "unsafe"})
  @CaptureSpan
  public SignRawTransactionWithWalletResponse singRawTransactionWithWallet(UTXOProvider utxoProvider, String walletId, String txHex) {
    return new SignRawTransactionWithWalletResponseWrapper(
      (Map<String, ?>) query(
        utxoProvider,
        walletId,
        "signrawtransactionwithwallet",
        txHex
      )
    );
  }

  @Override
  @CaptureSpan
  public String sendRawTransaction(UTXOProvider utxoProvider, String txHex) {
    return (String) query(utxoProvider, null, "sendrawtransaction", txHex);
  }

  @Override
  @SuppressWarnings({"unchecked", "unsafe"})
  @CaptureSpan
  public GetTrasactionResponse getTransaction(GetTransactionRequest request) {
    return new GetTrasactionResponseWrapper(
      (Map<String, ?>) query(
        request.getUtxoProvider(),
        request.getWalletId(),
        "gettransaction",
        request.getTxId(),
        request.isIncludeWatchOnly(),
        request.isVerbose()
      )
    );
  }

  @Override
  @CaptureSpan
  public void importMulti(UTXOProvider utxoProvider, String walletId, List<ImportMultiRequest> addresses, boolean rescan) {
    LinkedHashMap<String, Object> options = new LinkedHashMap<>() {
      {
        put("rescan", rescan);
      }
    };
    query(utxoProvider, walletId, "importmulti", addresses.stream().map(ImportMultiRequest::toJson).collect(Collectors.toList()), options);
  }

  @Override
  @SuppressWarnings({"unchecked", "unsafe"})
  @CaptureSpan
  public List<ListTransactionResponse> listTransactions(ListTransactionRequest request) {
    return new ListMapWrapper<>(
      (List<Map<String, ?>>) query(
        request.getUtxoProvider(),
        request.getWalletId(),
        "listtransactions",
        request.getLabel(),
        request.getCount(),
        request.getSkip(),
        request.isIncludeWatchOnly()
      )
    ) {
      @Override
      protected ListTransactionResponse wrap(Map<String, ?> m) {
        return new ListTransactionResponseWrapper(m);
      }
    };
  }

}

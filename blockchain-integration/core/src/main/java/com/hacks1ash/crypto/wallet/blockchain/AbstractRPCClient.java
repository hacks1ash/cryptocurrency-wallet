package com.hacks1ash.crypto.wallet.blockchain;

import co.elastic.apm.api.CaptureSpan;
import com.hacks1ash.crypto.wallet.blockchain.config.UTXOConfigProperties;
import com.hacks1ash.crypto.wallet.blockchain.factory.UTXOProvider;
import com.hacks1ash.crypto.wallet.blockchain.model.NetworkParams;
import com.hacks1ash.crypto.wallet.blockchain.model.UTXORawTxBuilder;
import com.hacks1ash.crypto.wallet.blockchain.model.request.*;
import com.hacks1ash.crypto.wallet.blockchain.model.response.*;
import com.hacks1ash.crypto.wallet.blockchain.model.response.impl.*;
import com.hacks1ash.crypto.wallet.blockchain.utils.HexCoder;
import com.hacks1ash.crypto.wallet.blockchain.utils.ListMapWrapper;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public abstract class AbstractRPCClient extends RPCClientImpl implements UTXORPCClient {

  public AbstractRPCClient(UTXOConfigProperties utxoConfigProperties) {
    super(utxoConfigProperties);
  }

  @Override
  @SuppressWarnings({"unchecked", "unsafe"})
  @CaptureSpan
  public CreateWalletResponse createWallet(CreateWalletRequest request, NetworkParams networkParams) {
    return new CreateWalletResponseWrapper(
      (Map<String, ?>) query(
        request.getUtxoProvider(),
        null,
        "createwallet",
        networkParams,
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
  public BigDecimal getBalance(GetBalanceRequest request, NetworkParams networkParams) {
    return (BigDecimal) query(
      request.getUtxoProvider(),
      request.getWalletId(),
      "getbalance",
      networkParams,
      "*",
      request.getMinConf(),
      request.isIncludeWatchonly(),
      request.isAvoidReuse()
    );
  }

  @Override
  @SuppressWarnings({"unchecked", "unsafe"})
  @CaptureSpan
  public EstimateSmartFeeResponse estimateSmartFee(EstimateSmartFeeRequest request, NetworkParams networkParams) {
    return new EstimateSmartFeeResponseWrapper(
      (Map<String, ?>) query(
        request.getUtxoProvider(),
        null,
        "estimatesmartfee",
        networkParams,
        request.getConfTargetBlock(),
        request.getEstimateMode().name()
      )
    );
  }

  @Override
  @CaptureSpan
  public void importPrivateKey(ImportPrivateKeyRequest request, NetworkParams networkParams) {
    queryForStream(
      request.getUtxoProvider(),
      request.getWalletId(),
      "importprivkey",
      networkParams,
      request.getPrivateKey(),
      request.getLabel(),
      request.isRescan()
    );
  }

  @Override
  @CaptureSpan
  public String createRawTransaction(UTXOProvider utxoProvider, String walletId, ArrayList<UTXORawTxBuilder.TxInput> inputs, List<UTXORawTxBuilder.TxOutput> outputs, NetworkParams networkParams) {
    List<Map<String, ?>> pInputs = new ArrayList<>();

    for (final UTXORawTxBuilder.TxInput txInput : inputs) {
      pInputs.add(new LinkedHashMap<>() {
        {
          put("txid", txInput.txid());
          put("vout", txInput.vout());
        }
      });
    }

    Map<String, Object> pOutputs = new LinkedHashMap<>();

    for (UTXORawTxBuilder.TxOutput txOutput : outputs) {
      pOutputs.put(txOutput.address(), txOutput.amount());
      if (txOutput.data() != null) {
        String hex = HexCoder.encode(txOutput.data());
        pOutputs.put("data", hex);
      }
    }

    return (String) query(utxoProvider, walletId, "createrawtransaction", networkParams, pInputs, pOutputs);
  }

  @Override
  @SuppressWarnings({"unchecked", "unsafe"})
  @CaptureSpan
  public FundRawTransactionResponse fundRawTransaction(FundRawTransactionRequest request, NetworkParams networkParams) {
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
        networkParams,
        request.getTxId(),
        options,
        request.isWitness()
      )
    );
  }

  @Override
  @SuppressWarnings({"unchecked", "unsafe"})
  @CaptureSpan
  public SignRawTransactionWithWalletResponse singRawTransactionWithWallet(UTXOProvider utxoProvider, String walletId, String txHex, NetworkParams networkParams) {
    return new SignRawTransactionWithWalletResponseWrapper(
      (Map<String, ?>) query(
        utxoProvider,
        walletId,
        "signrawtransactionwithwallet",
        networkParams,
        txHex
      )
    );
  }

  @Override
  @CaptureSpan
  public String sendRawTransaction(UTXOProvider utxoProvider, String txHex, NetworkParams networkParams) {
    return (String) query(utxoProvider, null, "sendrawtransaction", networkParams, txHex);
  }

  @Override
  @SuppressWarnings({"unchecked", "unsafe"})
  @CaptureSpan
  public GetTrasactionResponse getTransaction(GetTransactionRequest request, NetworkParams networkParams) {
    return new GetTrasactionResponseWrapper(
      (Map<String, ?>) query(
        request.getUtxoProvider(),
        request.getWalletId(),
        "gettransaction",
        networkParams,
        request.getTxId(),
        request.isIncludeWatchOnly(),
        request.isVerbose()
      )
    );
  }

  @Override
  @CaptureSpan
  public void importMulti(UTXOProvider utxoProvider, String walletId, List<ImportMultiRequest> addresses, boolean rescan, NetworkParams networkParams) {
    LinkedHashMap<String, Object> options = new LinkedHashMap<>() {
      {
        put("rescan", rescan);
      }
    };
    query(utxoProvider, walletId, "importmulti", networkParams, addresses.stream().map(ImportMultiRequest::toJson).collect(Collectors.toList()), options);
  }

  @Override
  @SuppressWarnings({"unchecked", "unsafe"})
  @CaptureSpan
  public List<ListTransactionResponse> listTransactions(ListTransactionRequest request, NetworkParams networkParams) {
    return new ListMapWrapper<>(
      (List<Map<String, ?>>) query(
        request.getUtxoProvider(),
        request.getWalletId(),
        "listtransactions",
        networkParams,
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

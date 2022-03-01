package com.hacks1ash.crypto.wallet.blockchain.model.request;

import com.hacks1ash.crypto.wallet.blockchain.factory.UTXOProvider;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FundRawTransactionRequest {

  private UTXOProvider utxoProvider;

  private String walletId;

  private String txId;

  private Boolean addInputs = true;

  private String changeAddress;

  private Integer changePosition;

  private String changeType;

  private Boolean includeWatching;

  private Boolean lockUnspents;

  private BigInteger feeRateBySatoshi;

  private BigDecimal feeRateByBTC;

  private List<Integer> subtractFeeFromOutputs = new ArrayList<>();

  private Boolean replaceable = false;

  private Integer confTarget;

  private EstimateSmartFeeRequest.EstimateMode estimateMode;

  private boolean isWitness;

}

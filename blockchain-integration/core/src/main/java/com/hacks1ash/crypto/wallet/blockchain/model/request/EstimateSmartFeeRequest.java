package com.hacks1ash.crypto.wallet.blockchain.model.request;

import com.hacks1ash.crypto.wallet.blockchain.factory.UTXOProvider;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
public class EstimateSmartFeeRequest {

  @NonNull
  private UTXOProvider utxoProvider;

  @NonNull
  private Integer confTargetBlock;

  private EstimateMode estimateMode = EstimateMode.CONSERVATIVE;

  public enum EstimateMode {
    UNSET, ECONOMICAL, CONSERVATIVE
  }

}

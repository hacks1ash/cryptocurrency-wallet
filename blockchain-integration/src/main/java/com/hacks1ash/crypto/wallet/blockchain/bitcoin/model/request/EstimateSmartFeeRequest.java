package com.hacks1ash.crypto.wallet.blockchain.bitcoin.model.request;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
public class EstimateSmartFeeRequest {

  @NonNull
  private Integer confTargetBlock;

  private EstimateMode estimateMode = EstimateMode.CONSERVATIVE;

  public enum EstimateMode {
    UNSET, ECONOMICAL, CONSERVATIVE
  }

}

package com.hacks1ash.crypto.wallet.core.model.response;

import com.hacks1ash.crypto.wallet.core.model.FeeUnit;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EstimateFeeResponse {

  private BigInteger fee;

  private BigInteger pricePerUnit;

  private FeeUnit feeUnit;

}

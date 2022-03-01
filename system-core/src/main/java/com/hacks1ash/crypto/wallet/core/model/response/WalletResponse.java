package com.hacks1ash.crypto.wallet.core.model.response;

import com.hacks1ash.crypto.wallet.core.model.CryptoCurrency;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// TODO add ticker to response
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WalletResponse {

  private String Id;

  private String name;

  private CryptoCurrency currency;

  private String hdSeed;

}

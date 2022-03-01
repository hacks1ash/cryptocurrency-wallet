package com.hacks1ash.crypto.wallet.core.storage.document;

import com.hacks1ash.crypto.wallet.core.model.CryptoCurrency;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class CurrencyConfig extends AbstractDocument {

  @Indexed(unique = true)
  private CryptoCurrency currency;

  private long numberOfConfirmationsRequired;

}

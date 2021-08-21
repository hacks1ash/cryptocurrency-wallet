package com.hacks1ash.crypto.wallet.core.storage.document;

import com.hacks1ash.crypto.wallet.core.model.Address;
import com.hacks1ash.crypto.wallet.core.model.CryptoCurrency;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;


@Data
@Document
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Wallet extends AbstractDocument {

  private String name;

  private String nodeWalletNameAlias;

  private CryptoCurrency currency;

  private List<String> hdSeed;

  private long creationTimestamp;

  private List<Address> addresses;

}

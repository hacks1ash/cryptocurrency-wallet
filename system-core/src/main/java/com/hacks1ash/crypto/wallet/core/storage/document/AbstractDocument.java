package com.hacks1ash.crypto.wallet.core.storage.document;

import lombok.Getter;
import org.springframework.data.annotation.Id;

@Getter
public class AbstractDocument {

  @Id
  private String id;

  @Override
  public boolean equals(Object obj) {

    if (this == obj) {
      return true;
    }

    if (this.id == null || obj == null || !(this.getClass().equals(obj.getClass()))) {
      return false;
    }

    AbstractDocument that = (AbstractDocument) obj;

    return this.id.equals(that.getId());
  }

  @Override
  public int hashCode() {
    return id == null ? 0 : id.hashCode();
  }
}

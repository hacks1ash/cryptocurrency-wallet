package com.hacks1ash.crypto.wallet.core;

import com.hacks1ash.crypto.wallet.core.model.CryptoCurrency;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class WalletException extends RuntimeException {

  private final String errorKey;

  private final String errorMessage;

  private final int errorCode;

  public WalletException(String errorKey, String errorMessage, int errorCode) {
    super(errorMessage);
    this.errorKey = errorKey;
    this.errorMessage = errorMessage;
    this.errorCode = errorCode;
  }

  public WalletException(Throwable cause, String errorKey, String errorMessage, int errorCode) {
    super(errorMessage, cause);
    this.errorKey = errorKey;
    this.errorMessage = errorMessage;
    this.errorCode = errorCode;
  }

  public static class InvalidHDSeed extends WalletException {
    public InvalidHDSeed(int size) {
      super("invalid.hd.seed", "HD seed should contain 12 words with space for separator, but got " + size + " words", 400);
    }
  }

  public static class WalletNotFound extends WalletException {
    public WalletNotFound(String walletId) {
      super("wallet.not.found", "Wallet with id -> " + walletId + " not found", 404);
    }
  }

  public static class CoinNotSupported extends WalletException {
    public CoinNotSupported(String shortName) {
      super("coin.not.supported", shortName + " currently not supported", 400);
    }
  }

  public static class UnknownTransactionType extends WalletException {
    public UnknownTransactionType(String type) {
      super("unknown.transaction.type", "Unknown transaction type -> " + type, 400);
    }
  }
}

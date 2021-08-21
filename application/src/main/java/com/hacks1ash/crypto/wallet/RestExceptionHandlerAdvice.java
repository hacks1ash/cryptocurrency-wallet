package com.hacks1ash.crypto.wallet;

import com.hacks1ash.crypto.wallet.core.WalletException;
import com.hacks1ash.crypto.wallet.core.model.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@RestControllerAdvice
public class RestExceptionHandlerAdvice {

  @ExceptionHandler(WalletException.class)
  private ResponseEntity<ErrorResponse> walletException(WalletException ex) {
    return new ResponseEntity<>(
      new ErrorResponse(ex.getErrorKey(), ex.getErrorMessage(), ex.getErrorCode()),
      HttpStatus.valueOf(ex.getErrorCode())
    );
  }

  @ExceptionHandler(RuntimeException.class)
  private ResponseEntity<ErrorResponse> runtimeException(RuntimeException ex) {
    return new ResponseEntity<>(
      new ErrorResponse("unable.to.process", "Unable to process request", 500),
      HttpStatus.INTERNAL_SERVER_ERROR
    );
  }

}

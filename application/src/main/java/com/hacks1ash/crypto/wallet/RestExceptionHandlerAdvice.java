package com.hacks1ash.crypto.wallet;

import com.hacks1ash.crypto.wallet.blockchain.GenericRpcException;
import com.hacks1ash.crypto.wallet.core.WalletException;
import com.hacks1ash.crypto.wallet.core.model.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class RestExceptionHandlerAdvice {

  @ExceptionHandler(WalletException.class)
  public ResponseEntity<ErrorResponse> walletException(WalletException ex) {
    return new ResponseEntity<>(
      new ErrorResponse(ex.getErrorKey(), ex.getErrorMessage(), ex.getErrorCode()),
      HttpStatus.valueOf(ex.getErrorCode())
    );
  }

  @ExceptionHandler(GenericRpcException.class)
  public ResponseEntity<ErrorResponse> genericRpcException(GenericRpcException ex) {
    return new ResponseEntity<>(
      new ErrorResponse(ex.getErrorKey(), ex.getErrorMessage(), ex.getErrorCode()),
      HttpStatus.valueOf(ex.getErrorCode())
    );
  }

  @ExceptionHandler(RuntimeException.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public ResponseEntity<ErrorResponse> runtimeException(RuntimeException ex) {
    return new ResponseEntity<>(
      new ErrorResponse("unable.to.process", "Unable to process request", 500),
      HttpStatus.INTERNAL_SERVER_ERROR
    );
  }

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
    Map<String, String> errors = new HashMap<>();
    ex.getBindingResult().getAllErrors().forEach((error) -> {
      String fieldName = ((FieldError) error).getField();
      String errorMessage = error.getDefaultMessage();
      errors.put(fieldName, errorMessage);
    });
    return errors;
  }

}

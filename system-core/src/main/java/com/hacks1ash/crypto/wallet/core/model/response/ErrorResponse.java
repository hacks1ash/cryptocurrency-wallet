package com.hacks1ash.crypto.wallet.core.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {

  private String errorKey;

  private String errorMessage;

  private int errorCode;

}

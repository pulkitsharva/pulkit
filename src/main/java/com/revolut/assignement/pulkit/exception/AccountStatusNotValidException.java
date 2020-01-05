package com.revolut.assignement.pulkit.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AccountStatusNotValidException extends Exception{

  private final ErrorCode errorCode;

  private final String message;
}

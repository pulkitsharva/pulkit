package com.revolut.assignement.pulkit.exception;

import lombok.Getter;

@Getter
public class RevolutExceptionMapper extends Exception{

  private ErrorCode errorCode;

  private String message;
}

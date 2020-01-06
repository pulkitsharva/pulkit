package com.revolut.assignement.pulkit.exception;

import lombok.Getter;

public enum ErrorCode {
  INVALID_ACCOUNT("There is no account associated with the requested accountNumber"),
  INSUFFICIENT_BALANCE("There is insufficient balance in the requested account"),
  INVALID_TRANACTION("There is no transaction associated with the requested transactionId"),
  INVALID_ACCOUNT_STATUS("Account status is not valid for this transaction"),
  DUPLICATE_TRANSFER_REQUEST("There is already a transfer request with the given pgTransactionId");

  @Getter private final String message;

  ErrorCode(final String message) {
    this.message = message;
  }
}

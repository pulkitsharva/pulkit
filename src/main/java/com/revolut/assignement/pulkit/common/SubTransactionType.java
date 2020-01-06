package com.revolut.assignement.pulkit.common;

public enum SubTransactionType {

  TRANSACTION(TransactionType.DEBIT),CASHBACK(TransactionType.CREDIT),MONEY_TRANSFER(TransactionType.CREDIT),REFUND(TransactionType.CREDIT);
  TransactionType type;
  SubTransactionType(TransactionType type) {
    this.type = type;
  }

  public TransactionType getTransactionType(){
    return this.type;
  }
}

package com.revolut.assignement.pulkit.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class TransactionMetadata {

  private CurrencyType currency;
  private TransactionPlatform transactionPlatform;
  private String receiverAccountNumber;
  private String senderAccountNumber;
}

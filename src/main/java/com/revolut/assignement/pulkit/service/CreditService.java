package com.revolut.assignement.pulkit.service;

import com.revolut.assignement.pulkit.dao.Credit;
import com.revolut.assignement.pulkit.dto.MoneyTransferRequestDto;
import java.util.List;

public interface CreditService {

  void insert(final Credit credit);

  void update(final Credit credit);

  List<Credit> getCreditsByAccountNumber(final String accountNumber);

  Credit getCreditByTransactionId(final Long transactionId);

  Credit postCreditTransactionAtDestinationAccount(final String accountNumber, final MoneyTransferRequestDto requestDto);
}

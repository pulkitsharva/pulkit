package com.revolut.assignement.pulkit.service;

import com.revolut.assignement.pulkit.dto.MoneyTransferRequestDto;
import com.revolut.assignement.pulkit.exception.AccountNotFoundException;
import com.revolut.assignement.pulkit.exception.AccountStatusNotValidException;
import com.revolut.assignement.pulkit.exception.DuplicateTransferRequestException;
import com.revolut.assignement.pulkit.exception.InsufficientBalanceException;

public interface TransactionService {

  void doTransfer(final String accountNumber, final MoneyTransferRequestDto requestDto)
      throws AccountNotFoundException, AccountStatusNotValidException, InsufficientBalanceException, DuplicateTransferRequestException;

}

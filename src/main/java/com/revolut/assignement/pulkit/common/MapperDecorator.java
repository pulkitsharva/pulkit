package com.revolut.assignement.pulkit.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.inject.Inject;
import com.revolut.assignement.pulkit.dao.Statement;
import com.revolut.assignement.pulkit.dao.Transactions;
import com.revolut.assignement.pulkit.dto.MoneyTransferRequestDto;
import com.revolut.assignement.pulkit.dto.StatementMetadata;
import com.revolut.assignement.pulkit.dto.StatementResponseDto;
import com.revolut.assignement.pulkit.dto.TransactionMetadata;
import java.util.List;

public class MapperDecorator {
/*
  @Inject
  private Imapper imapper;
  @Override
  Transactions transferRequestToDebitEntity(final MoneyTransferRequestDto request,
      final String accountNumber, final SubTransactionType subTransactionType,
      final TransactionStatus transactionStatus, final String metadata) {
    return imapper.transferRequestToDebitEntity(request, accountNumber, subTransactionType, transactionStatus, metadata);
  }

  @Override
  TransactionMetadata transferRequestToTransactionMetadata(
      final MoneyTransferRequestDto moneyTransferRequestDto, final String senderAccountNumber) {
    return imapper.transferRequestToTransactionMetadata(moneyTransferRequestDto, senderAccountNumber);
  }

  @Override
  List<StatementResponseDto> map(final List<Statement> statementList) {

  }

  @Override
  StatementResponseDto transferStatementToStatementResponseDto(final Statement statement) {
    return null;
  }

  @Override
  StatementMetadata transferTransactionToStatementMetadata(final Transactions transactions) {
    return null;
  }

  @Override
  String fromDtotoJsonString(final Object object) throws JsonProcessingException {
    return null;
  }*/
}

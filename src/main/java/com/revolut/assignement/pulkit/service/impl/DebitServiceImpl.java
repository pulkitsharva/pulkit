package com.revolut.assignement.pulkit.service.impl;

import com.google.inject.Inject;
import com.revolut.assignement.pulkit.common.CommonsUtil;
import com.revolut.assignement.pulkit.common.Imapper;
import com.revolut.assignement.pulkit.common.SubTransactionType;
import com.revolut.assignement.pulkit.common.TransactionMetadata;
import com.revolut.assignement.pulkit.common.TransactionStatus;
import com.revolut.assignement.pulkit.dao.Debit;
import com.revolut.assignement.pulkit.dto.MoneyTransferRequestDto;
import com.revolut.assignement.pulkit.repository.DebitRepository;
import com.revolut.assignement.pulkit.service.DebitService;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DebitServiceImpl implements DebitService {

  @Inject
  private DebitRepository debitRepository;

  @Override
  public void insert(final Debit debit) {
    debitRepository.insert(debit);
  }

  @Override
  public void update(Debit debit) {
    debitRepository.update(debit);
  }

  @Override
  public List<Debit> getDebitsByAccountNumber(String accountNumber) {
    return debitRepository.getDebitsByAccountNumber(accountNumber);
  }

  @Override
  public Debit getDebitByTransactionId(Long transactionId) {
    return debitRepository.getDebitByTransactionId(transactionId);
  }

  public Debit postDebitTransactionAtSourceAccount(final String accountNumber, final MoneyTransferRequestDto requestDto){
    try{
      TransactionMetadata transactionMetadata = Imapper.INSTANCE.transferRequestToTransactionMetadata(requestDto, accountNumber);
      String transactionMetadataString = Imapper.INSTANCE.fromDtotoJsonString(transactionMetadata);
      Debit debit = Imapper.INSTANCE.transferRequestToDebitEntity(requestDto, accountNumber, SubTransactionType.MONEY_TRANSFER, TransactionStatus.SUCCESS, transactionMetadataString);
      debitRepository.insert(debit);
      String externalReferenceId = CommonsUtil.getExternalReferenceId(debit.getTransactionId());
      debit.setExternalReferenceId(externalReferenceId);
      debitRepository.update(debit);
      log.info(
          "Debit transaction of amount:{} posted successfully at account:{}",
          debit.getAmount(),
          debit.getAccountNumber());
      return debit;
    }
    catch (Exception e){
      log.error("Unable to post transaction for account:{}, rolling back. Error:{}", accountNumber, e);
      throw new RuntimeException("Unable to post transaction for account:{}, rolling back.");
    }
  }
}

package com.revolut.assignement.pulkit.service.impl;

import com.google.inject.Inject;
import com.revolut.assignement.pulkit.common.CommonsUtil;
import com.revolut.assignement.pulkit.common.Imapper;
import com.revolut.assignement.pulkit.common.SubTransactionType;
import com.revolut.assignement.pulkit.common.TransactionMetadata;
import com.revolut.assignement.pulkit.common.TransactionStatus;
import com.revolut.assignement.pulkit.dao.Credit;
import com.revolut.assignement.pulkit.dto.MoneyTransferRequestDto;
import com.revolut.assignement.pulkit.repository.CreditRepository;
import com.revolut.assignement.pulkit.service.CreditService;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CreditServiceImpl implements CreditService {

  @Inject
  private CreditRepository creditRepository;

  @Override
  public void insert(final Credit credit) {
    creditRepository.insert(credit);
  }

  @Override
  public void update(final Credit credit) {
    creditRepository.update(credit);
  }

  @Override
  public List<Credit> getCreditsByAccountNumber(final String accountNumber) {
    return creditRepository.getCreditsByAccountNumber(accountNumber);
  }

  @Override
  public Credit getCreditByTransactionId(final Long transactionId) {
    return creditRepository.getCreditByTransactionId(transactionId);
  }

  public Credit postCreditTransactionAtDestinationAccount(final String accountNumber, final MoneyTransferRequestDto requestDto){
    try{
      TransactionMetadata transactionMetadata = Imapper.INSTANCE.transferRequestToTransactionMetadata(requestDto, accountNumber);
      String transactionMetadataString = Imapper.INSTANCE.fromDtotoJsonString(transactionMetadata);
      Credit credit = Imapper.INSTANCE.transferRequestToCreditEntity(requestDto, requestDto.getReceiverAccountNumber(), SubTransactionType.MONEY_TRANSFER, TransactionStatus.SUCCESS, transactionMetadataString);
      creditRepository.insert(credit);
      String externalReferenceId = CommonsUtil.getExternalReferenceId(credit.getTransactionId());
      credit.setExternalReferenceId(externalReferenceId);
      creditRepository.update(credit);
      log.info(
          "Credit transaction of amount:{} posted successfully at account:{}",
          credit.getAmount(),
          credit.getAccountNumber());
      return credit;
    }
    catch (Exception e){
      log.error("Unable to post transaction for account:{}, rolling back. Error:{}", accountNumber, e);
      throw new RuntimeException("Unable to post transaction for account:{}, rolling back.");
    }

  }
}

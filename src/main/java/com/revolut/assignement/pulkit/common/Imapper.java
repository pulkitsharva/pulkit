package com.revolut.assignement.pulkit.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.revolut.assignement.pulkit.dao.Transactions;
import com.revolut.assignement.pulkit.dto.MoneyTransferRequestDto;
import java.util.Objects;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper
public interface Imapper {

  Imapper INSTANCE = Mappers.getMapper(Imapper.class);
  ObjectMapper objectMapper = new ObjectMapper();

  @Mapping(source = "accountNumber", target = "accountNumber")
  @Mapping(source = "subTransactionType", target = "type")
  @Mapping(source = "transactionStatus", target = "status")
  @Mapping(source = "metadata", target = "metadata")
  Transactions transferRequestToDebitEntity(final MoneyTransferRequestDto request, final String accountNumber, final SubTransactionType subTransactionType, final TransactionStatus transactionStatus, String metadata);

  TransactionMetadata transferRequestToTransactionMetadata(final MoneyTransferRequestDto moneyTransferRequestDto, final String senderAccountNumber);

  @Named("fromDtotoJsonString")
  default String fromDtotoJsonString(Object object) throws JsonProcessingException {
    if (Objects.nonNull(object)) {
      String result = objectMapper.writeValueAsString(object);
      return result;
    }
    return null;
  }
}

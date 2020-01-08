package com.revolut.assignement.pulkit.common;

import static org.reflections.util.ConfigurationBuilder.build;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Strings;
import com.revolut.assignement.pulkit.dao.Statement;
import com.revolut.assignement.pulkit.dao.Transactions;
import com.revolut.assignement.pulkit.dto.MoneyTransferRequestDto;
import com.revolut.assignement.pulkit.dto.StatementMetadata;
import com.revolut.assignement.pulkit.dto.StatementPassbookDto;
import com.revolut.assignement.pulkit.dto.StatementResponseDto;
import com.revolut.assignement.pulkit.dto.TransactionMetadata;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
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

  @Named("transferStatementToStatementResponseDto")
  default StatementResponseDto map(final List<Statement> statementList){
    List<StatementPassbookDto> statementResponseDtos = new ArrayList<>();
    for(Statement statement : statementList){
      StatementPassbookDto statementPassbookDto = transferStatementToStatementResponseDto(statement);
      statementResponseDtos.add(statementPassbookDto);
    }
    return StatementResponseDto.builder().statements(statementResponseDtos).build();
  }

  default StatementPassbookDto transferStatementToStatementResponseDto(final Statement statement){
    StatementMetadata statementMetadata = null;
    try{
      statementMetadata = (StatementMetadata) fromJsonStringToDto(statement.getMetadata(), StatementMetadata.class);
    }
    catch (Exception exception){
    }

    StatementPassbookDto statementPassbookDto = StatementPassbookDto.builder()
        .amount(statement.getAmount())
        .status(statement.getStatus())
        .transactionType(statement.getType())
        .updatedAt(new DateTime(statement.getUpdatedAt()))
        .externalReferenceId( statementMetadata!=null ? statementMetadata.getExternalReferenceId() : "")
        .comment(statementMetadata!=null ? statementMetadata.getComment() : "")
        .build();
    return statementPassbookDto;
  }

  StatementMetadata transferTransactionToStatementMetadata(final Transactions transactions);

  @Named("fromDtotoJsonString")
  default String fromDtotoJsonString(Object object) throws JsonProcessingException {
    if (Objects.nonNull(object)) {
      String result = objectMapper.writeValueAsString(object);
      return result;
    }
    return null;
  }

  @Named("fromJsonStringToDto")
  default Object fromJsonStringToDto(final String jsonString, Class destination)
      throws IOException {
    if(!Strings.isNullOrEmpty(jsonString)){
      Object object = objectMapper.readValue(jsonString,destination);
      return object;
    }
    return null;
  }
}

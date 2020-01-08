package com.revolut.assignement.pulkit.dto;

import com.revolut.assignement.pulkit.common.TransactionStatus;
import com.revolut.assignement.pulkit.common.TransactionType;
import java.math.BigDecimal;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.joda.time.DateTime;

@Getter
@Setter
@ToString
@Builder
public class StatementPassbookDto {

  private String externalReferenceId;
  private BigDecimal amount;
  private TransactionType transactionType;
  private TransactionStatus status;
  private String comment;
  private DateTime updatedAt;
}

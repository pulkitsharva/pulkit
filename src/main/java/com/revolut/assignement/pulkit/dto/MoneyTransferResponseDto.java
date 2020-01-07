package com.revolut.assignement.pulkit.dto;

import com.revolut.assignement.pulkit.common.TransactionStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class MoneyTransferResponseDto {

  private String paymentGatewayTransactionId;
  private String externalReferenceId;
  private TransactionStatus status;
  private Long timestamp;
}

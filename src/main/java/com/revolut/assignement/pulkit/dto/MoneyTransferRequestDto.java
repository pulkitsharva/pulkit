package com.revolut.assignement.pulkit.dto;

import com.revolut.assignement.pulkit.common.CurrencyType;
import com.revolut.assignement.pulkit.common.TransactionPlatform;
import java.math.BigDecimal;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.constraints.Range;

@Getter
@Setter
@AllArgsConstructor
@ToString
@NoArgsConstructor
public class MoneyTransferRequestDto {

  @NotNull
  @NotEmpty
  private String receiverAccountNumber;

  @NotNull
  @NotEmpty
  private String paymentGatewayTransactionId;

  private String comment;

  @NotNull
  @Range(min = 1, max=10000)
  private BigDecimal amount;

  @NotNull
  private TransactionPlatform transactionPlatform;

  private String originIP;

  @NotNull
  private CurrencyType currency;

  @NotNull
  private Long timestamp;

}

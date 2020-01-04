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

@Getter
@Setter
@AllArgsConstructor
@ToString
@NoArgsConstructor
public class MoneyTransferRequestDto {

  @NotNull
  @NotEmpty
  private String receiverAccountNumber;

  private String comment;

  @NotNull
  private BigDecimal amount;

  @NotNull
  private TransactionPlatform transactionPlatform;

  private String originIP;

  @NotNull
  private CurrencyType currency;

  @NotNull
  private Long timestamp;

}

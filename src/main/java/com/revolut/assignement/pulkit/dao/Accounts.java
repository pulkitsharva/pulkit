package com.revolut.assignement.pulkit.dao;

import com.revolut.assignement.pulkit.common.AccountStatus;
import java.math.BigDecimal;
import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.UpdateTimestamp;
import org.joda.time.DateTime;

@Entity
@Table(name = "accounts")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@DynamicUpdate
@Builder
public class Accounts {

  @Id
  @Column(name = "account_number", nullable = false)
  private String accountNumber;

  @Column(name = "user_id", nullable = false)
  private Long userId;

  @Column(name = "available_balance", nullable = false)
  private BigDecimal availableBalance;

  @Column(name = "created_at", nullable = false)
  @CreationTimestamp
  private Timestamp createdAt;

  @Column(name = "updated_at")
  @UpdateTimestamp
  private Timestamp updatedAt;

  @Column(name = "account_activation_date")
  private DateTime accountActivationDate;

  @Column(name = "account_status", nullable = false)
  private AccountStatus accountStatus;

}

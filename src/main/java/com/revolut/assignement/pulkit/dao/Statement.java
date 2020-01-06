package com.revolut.assignement.pulkit.dao;

import com.revolut.assignement.pulkit.common.TransactionStatus;
import com.revolut.assignement.pulkit.common.TransactionType;
import java.math.BigDecimal;
import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "statement")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@DynamicUpdate
@Builder
public class Statement {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "statement_id", nullable = false)
  private Long statementId;

  @Column(name = "account_number", nullable = false)
  private String accountNumber;

  @Column(name = "type", nullable = false)
  private TransactionType type;

  @Column(name = "status", nullable = false)
  private TransactionStatus status;

  @Column(name = "reference_id", nullable = false)
  private Long referenceId;

  @Column(name = "amount", nullable = false)
  private BigDecimal amount;

  @Column(name = "created_at", nullable = false)
  @CreationTimestamp
  private Timestamp createdAt;

  @Column(name = "updated_at")
  @UpdateTimestamp
  private Timestamp updatedAt;

  //add visible to book flag
  //add builder to construct statement from transactions
}

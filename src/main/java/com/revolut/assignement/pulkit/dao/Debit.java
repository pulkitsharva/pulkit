package com.revolut.assignement.pulkit.dao;

import com.revolut.assignement.pulkit.common.SubTransactionType;
import com.revolut.assignement.pulkit.common.TransactionStatus;
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
@Table(name = "debit")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@DynamicUpdate
@Builder

public class Debit {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "transaction_id", nullable = false)
  private Long transactionId;

  @Column(name = "account_number", nullable = false)
  private String accountNumber;

  @Column(name = "external_reference_id")
  private String externalReferenceId;

  @Column(name = "type", nullable = false)
  private SubTransactionType type;

  @Column(name = "status", nullable = false)
  private TransactionStatus status;

  @Column(name = "amount", nullable = false)
  private BigDecimal amount;

  @Column(name = "origin_ip", nullable = false)
  private String originIP;

  @Column(name = "comment")
  private String comment;


  @Column(name = "created_at", nullable = false)
  @CreationTimestamp
  private Timestamp createdAt;

  @Column(name = "updated_at")
  @UpdateTimestamp
  private Timestamp updatedAt;

  @Column(name = "metadata")
  private String metadata;
}

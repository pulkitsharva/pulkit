package com.revolut.assignement.pulkit.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class StatementMetadata {

  private String externalReferenceId;
  private String comment;
}

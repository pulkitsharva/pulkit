package com.revolut.assignement.pulkit.dto;

import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
@Builder
public class StatementResponseDto {

  private List<StatementPassbookDto> statements;
}

package com.revolut.assignement.pulkit.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;
import io.dropwizard.db.DataSourceFactory;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RevolutApplicationConfiguration extends Configuration {

  @Valid
  @NotNull
  @JsonProperty("data_source_factory")
  private DataSourceFactory dataSourceFactory;
}

package com.revolut.assignement.pulkit.controller;

import com.google.inject.Inject;
import com.revolut.assignement.pulkit.dao.Statement;
import com.revolut.assignement.pulkit.dto.StatementResponseDto;
import com.revolut.assignement.pulkit.service.StatementService;
import java.util.List;
import javax.validation.constraints.NotNull;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Produces(MediaType.APPLICATION_JSON)
@Path("/api/v1/user")
public class UserController {

  @Inject
  private StatementService statementService;

  @GET
  @Path("/statement")
  public Response getUserStatement(@NotNull @HeaderParam("X-ACCOUNT-NUMBER") final String accountNumber){
    StatementResponseDto statementResponseDto = this.statementService.getStatementByAccountNumber(accountNumber);
    return Response.ok().entity(statementResponseDto).build();

  }
}

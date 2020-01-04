package com.revolut.assignement.pulkit.controller;

import com.revolut.assignement.pulkit.dto.MoneyTransferRequestDto;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Produces(MediaType.APPLICATION_JSON)
@Path("/api/v1/transact")
public class TransactController {

  @POST
  @Path("/transfer")
  @Consumes(MediaType.APPLICATION_JSON)
  public Response transferMoney(@NotNull @HeaderParam("X-ACCOUNT-NUMBER") final String accountNumber, @Valid @NotNull final MoneyTransferRequestDto request){
    log.info("got request:{}", request);
    return Response.ok().build();
  }

}

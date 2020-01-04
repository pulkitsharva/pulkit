package com.revolut.assignement.pulkit.controller;

import com.google.inject.Inject;
import com.revolut.assignement.pulkit.common.AccountStatus;
import com.revolut.assignement.pulkit.dao.Accounts;
import com.revolut.assignement.pulkit.dao.User;
import com.revolut.assignement.pulkit.repository.AccountsRepository;
import com.revolut.assignement.pulkit.repository.UserRepository;
import io.dropwizard.hibernate.UnitOfWork;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;

@Path("/test")
@Produces(MediaType.APPLICATION_JSON)
@Slf4j
public class TestController {

  @Inject private UserRepository userRepository;

  @Inject
  private AccountsRepository accountsRepository;
  @GET
  @UnitOfWork
  @Path("/test1")
  public Response test() {
    User user =
        User.builder()
            .email(null)
            .firstName("pulkit")
            .lastName("sharva")
            .phoneNumber("+919654465464")
            .build();
    userRepository.create(user);
    Accounts accounts =
        Accounts.builder()
            .accountNumber(UUID.randomUUID().toString())
            .userId(user.getUserId())
            .availableBalance(new BigDecimal(1000))
            .accountStatus(AccountStatus.ACTIVE)
            .build();
    accountsRepository.insert(accounts);
    return Response.ok().build();
  }

  @GET
  @Path("/test2")
  @UnitOfWork
  public List<Accounts> getUser(@QueryParam("userId") final Long userId) {
    return accountsRepository.getAllAccounts();
  }
}

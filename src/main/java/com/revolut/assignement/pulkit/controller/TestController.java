package com.revolut.assignement.pulkit.controller;

import com.google.inject.Inject;
import com.revolut.assignement.pulkit.common.AccountStatus;
import com.revolut.assignement.pulkit.dao.Accounts;
import com.revolut.assignement.pulkit.dao.User;
import com.revolut.assignement.pulkit.repository.AccountsRepository;
import com.revolut.assignement.pulkit.repository.UserRepository;
import io.dropwizard.hibernate.UnitOfWork;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import lombok.extern.slf4j.Slf4j;

@Path("/debug")
@Produces(MediaType.APPLICATION_JSON)
@Slf4j
public class TestController {

  @Inject private UserRepository userRepository;

  @Inject
  private AccountsRepository accountsRepository;

  @POST
  @UnitOfWork
  @Path("/account")
  public List<Accounts> createAccount() {
      User user1 =
          User.builder()
              .email(null)
              .firstName("pulkit")
              .lastName("sharva")
              .phoneNumber("+911234567899")
              .build();
      userRepository.create(user1);
      Accounts accounts1 =
          Accounts.builder()
              .accountNumber(UUID.randomUUID().toString())
              .userId(user1.getUserId())
              .availableBalance(new BigDecimal(1000))
              .accountStatus(AccountStatus.ACTIVE)
              .build();
      accountsRepository.insert(accounts1);

      User user2 =
          User.builder()
              .email("john@gmail.com")
              .firstName("john")
              .lastName("travolta")
              .phoneNumber("+919876543210")
              .build();
      userRepository.create(user2);
      Accounts accounts2 =
          Accounts.builder()
              .accountNumber(UUID.randomUUID().toString())
              .userId(user2.getUserId())
              .availableBalance(new BigDecimal(1500))
              .accountStatus(AccountStatus.ACTIVE)
              .build();
      accountsRepository.insert(accounts2);
      List<Accounts> list = new ArrayList<>();
      list.add(accounts1);
      list.add(accounts2);
      return list;
  }

}

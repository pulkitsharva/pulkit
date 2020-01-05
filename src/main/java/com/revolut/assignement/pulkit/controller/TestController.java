package com.revolut.assignement.pulkit.controller;

import com.google.inject.Inject;
import com.revolut.assignement.pulkit.common.AccountStatus;
import com.revolut.assignement.pulkit.dao.Accounts;
import com.revolut.assignement.pulkit.dao.Credit;
import com.revolut.assignement.pulkit.dao.Debit;
import com.revolut.assignement.pulkit.dao.Statement;
import com.revolut.assignement.pulkit.dao.User;
import com.revolut.assignement.pulkit.repository.AccountsRepository;
import com.revolut.assignement.pulkit.repository.CreditRepository;
import com.revolut.assignement.pulkit.repository.DebitRepository;
import com.revolut.assignement.pulkit.repository.StatementRepository;
import com.revolut.assignement.pulkit.repository.UserRepository;
import com.revolut.assignement.pulkit.service.StatementService;
import io.dropwizard.hibernate.UnitOfWork;
import java.math.BigDecimal;
import java.util.ArrayList;
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

  @Inject
  private StatementRepository statementRepository;

  @Inject
  private DebitRepository debitRepository;

  @Inject
  private CreditRepository creditRepository;
  @GET
  @UnitOfWork
  @Path("/test1")
  public List<String> test() {
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
            .accountNumber("xxx")
            .userId(user.getUserId())
            .availableBalance(new BigDecimal(1000))
            .accountStatus(AccountStatus.ACTIVE)
            .build();
    accountsRepository.insert(accounts);

    User user1 =
        User.builder()
            .email("john@gmail.com")
            .firstName("john")
            .lastName("travolta")
            .phoneNumber("+919654465464")
            .build();
    userRepository.create(user1);
    Accounts accounts1 =
        Accounts.builder()
            .accountNumber("yyy")
            .userId(user1.getUserId())
            .availableBalance(new BigDecimal(500))
            .accountStatus(AccountStatus.ACTIVE)
            .build();
    accountsRepository.insert(accounts1);
    List<String> list = new ArrayList<>();
    list.add(accounts.getAccountNumber());
    list.add(accounts1.getAccountNumber());
    return list;
  }

  @GET
  @Path("/test2")
  @UnitOfWork
  public List<Accounts> getUser(@QueryParam("userId") final Long userId) {
    return accountsRepository.getAllAccounts();
  }

  @GET
  @Path("/test3")
  @UnitOfWork
  public List<Statement> getAllStatements() {
    return statementRepository.getAll();
  }

  @GET
  @Path("/test4")
  @UnitOfWork
  public List<Credit> getAllCredits() {
    return creditRepository.getCreditsByAccountNumber("yyy");
  }

  @GET
  @Path("/test5")
  @UnitOfWork
  public List<Debit> getAllDebits() {
    return debitRepository.getDebitsByAccountNumber("xxx");
  }
}

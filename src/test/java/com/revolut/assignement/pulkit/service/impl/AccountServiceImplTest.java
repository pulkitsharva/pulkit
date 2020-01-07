package com.revolut.assignement.pulkit.service.impl;


import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

import com.revolut.assignement.pulkit.common.AccountStatus;
import com.revolut.assignement.pulkit.dao.Accounts;
import com.revolut.assignement.pulkit.repository.AccountsRepository;
import java.math.BigDecimal;
import java.sql.Timestamp;
import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class AccountServiceImplTest {

  AccountsRepository accountsRepository = mock(AccountsRepository.class);

  @InjectMocks
  AccountServiceImpl accountService;

  Accounts successAccount = new Accounts("xxx",123L,new BigDecimal(100), new Timestamp(DateTime.now().getMillis()), null, null,AccountStatus.ACTIVE);

  @Before
  public void setup(){
    doReturn(successAccount).when(accountsRepository).getAccountByAccountNumber(eq(successAccount.getAccountNumber()));
  }

  @Test
  public void getAccountByAccountNumberSuccess() {
    Accounts testAccount = this.accountService.getAccountByAccountNumber("xxx");
    Assert.assertEquals(successAccount,testAccount);
  }


}
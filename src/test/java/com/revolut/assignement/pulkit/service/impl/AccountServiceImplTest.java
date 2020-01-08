package com.revolut.assignement.pulkit.service.impl;


import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;

import com.revolut.assignement.pulkit.common.AccountStatus;
import com.revolut.assignement.pulkit.dao.Accounts;
import com.revolut.assignement.pulkit.repository.AccountsRepository;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;
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
  Accounts nullAccountNumber = new Accounts(null,123L,new BigDecimal(100), new Timestamp(DateTime.now().getMillis()), null, null,AccountStatus.ACTIVE);
  List<Accounts> accountsList = Arrays.asList(successAccount, nullAccountNumber);

  @Before
  public void setup(){


  }

  @Test
  public void getAccountByAccountNumberSuccess() {
    doReturn(successAccount).when(accountsRepository).getAccountByAccountNumber(eq(successAccount.getAccountNumber()));
    Accounts testAccount = this.accountService.getAccountByAccountNumber("xxx");
    Assert.assertEquals(successAccount,testAccount);
  }

  @Test
  public void getAccountByAccountNumberNotFound() {
    Accounts testAccount = this.accountService.getAccountByAccountNumber("yyy");
    Assert.assertNull(testAccount);
  }

  @Test(expected = RuntimeException.class)
  public void getAccountByAccountNumberFailWithNull() {

      this.accountService.getAccountByAccountNumber(null);
  }

  @Test
  public void insertSuccess(){
    Accounts accounts = new Accounts("xxx",123L,new BigDecimal(100), new Timestamp(DateTime.now().getMillis()), null, null,AccountStatus.ACTIVE);
    this.accountService.insert(accounts);
    Assert.assertEquals(accounts.getAccountNumber(), successAccount.getAccountNumber());
    Assert.assertEquals(accounts.getUserId(), successAccount.getUserId());
    Assert.assertEquals(accounts.getAvailableBalance(), successAccount.getAvailableBalance());
    Assert.assertEquals(accounts.getAccountNumber(), successAccount.getAccountNumber());

  }

  @Test(expected = RuntimeException.class)
  public void insertFailWithNull(){

      this.accountService.insert(null);
  }

  @Test(expected = org.hibernate.id.IdentifierGenerationException.class)
  public void insertFailIfAccountNumberIsNotSet(){
    doThrow(org.hibernate.id.IdentifierGenerationException.class).when(accountsRepository).insert(eq(nullAccountNumber));
    this.accountService.insert(nullAccountNumber);
  }

  @Test
  public void updateSuccess(){
    Accounts accounts = new Accounts("xxx",123L,new BigDecimal(101), new Timestamp(DateTime.now().getMillis()), null, null,AccountStatus.ACTIVE);
    this.accountService.update(accounts);
    Assert.assertEquals(accounts.getAccountNumber(), successAccount.getAccountNumber());
    Assert.assertEquals(accounts.getUserId(), successAccount.getUserId());
    Assert.assertEquals(accounts.getAvailableBalance(), successAccount.getAvailableBalance().add(new BigDecimal(1)));
    Assert.assertEquals(accounts.getAccountNumber(), successAccount.getAccountNumber());

  }

  @Test(expected = RuntimeException.class)
  public void updateFailWithNull(){
    this.accountService.update(null);
  }

  @Test
  public void getAllAccountSuccess(){
    doReturn(accountsList).when(accountsRepository).getAllAccounts();
    List<Accounts> accountsListResponse = this.accountService.getAllAccounts();
    Assert.assertEquals(accountsList, accountsListResponse);
  }

  @Test
  public void getAllAccountEmpty(){
    List<Accounts> accountsListResponse = this.accountService.getAllAccounts();
    Assert.assertEquals(Arrays.asList(), accountsListResponse);
  }

  @Test
  public void getAccountByAccountNumberWithLockSuccess(){
    doReturn(successAccount).when(accountsRepository).getAccountByAccountNumberWithLock(eq(successAccount.getAccountNumber()));
    Accounts account = this.accountService.getAccountByAccountNumberWithLock("xxx");
    Assert.assertEquals(successAccount, account);
  }

  @Test
  public void getAccountByAccountNumberWithLockNull(){
    Accounts account = this.accountService.getAccountByAccountNumberWithLock("yyy");
    Assert.assertNull(account);
  }

  @Test
  public void reduceBalanceOnSuccessfulTransactionSuccess(){
    Accounts accounts = new Accounts("xxx",123L,new BigDecimal(101), new Timestamp(DateTime.now().getMillis()), null, null,AccountStatus.ACTIVE);
    this.accountService.increaseBalanceOnSuccessfulTransaction(accounts, new BigDecimal(10));
  }

  @Test
  public void increaseBalanceOnSuccessfulTransactionSuccess(){
    Accounts accounts = new Accounts("xxx",123L,new BigDecimal(101), new Timestamp(DateTime.now().getMillis()), null, null,AccountStatus.ACTIVE);
    this.accountService.reduceBalanceOnSuccessfulTransaction(accounts, new BigDecimal(10));
  }

  @Test(expected = RuntimeException.class)
  public void reduceBalanceOnSuccessfulTransactionFail(){
    Accounts accounts = new Accounts("xxx",123L,null, new Timestamp(DateTime.now().getMillis()), null, null,AccountStatus.ACTIVE);
    this.accountService.increaseBalanceOnSuccessfulTransaction(accounts, new BigDecimal(10));
  }

  @Test(expected = RuntimeException.class)
  public void increaseBalanceOnSuccessfulTransactionFail(){
    Accounts accounts = new Accounts("xxx",123L,new BigDecimal(101), new Timestamp(DateTime.now().getMillis()), null, null,AccountStatus.ACTIVE);
    this.accountService.reduceBalanceOnSuccessfulTransaction(accounts, null);
  }

}
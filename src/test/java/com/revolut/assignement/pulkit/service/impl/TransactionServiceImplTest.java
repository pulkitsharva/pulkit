package com.revolut.assignement.pulkit.service.impl;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;

import com.revolut.assignement.pulkit.common.AccountStatus;
import com.revolut.assignement.pulkit.common.CurrencyType;
import com.revolut.assignement.pulkit.common.SubTransactionType;
import com.revolut.assignement.pulkit.common.TransactionPlatform;
import com.revolut.assignement.pulkit.common.TransactionStatus;
import com.revolut.assignement.pulkit.dao.Accounts;
import com.revolut.assignement.pulkit.dao.Transactions;
import com.revolut.assignement.pulkit.dto.MoneyTransferRequestDto;
import com.revolut.assignement.pulkit.dto.MoneyTransferResponseDto;
import com.revolut.assignement.pulkit.exception.AccountNotFoundException;
import com.revolut.assignement.pulkit.exception.AccountStatusNotValidException;
import com.revolut.assignement.pulkit.exception.DuplicateTransferRequestException;
import com.revolut.assignement.pulkit.exception.InsufficientBalanceException;
import com.revolut.assignement.pulkit.repository.TransactionsRepository;
import com.revolut.assignement.pulkit.service.AccountService;
import com.revolut.assignement.pulkit.service.StatementService;
import java.math.BigDecimal;
import java.sql.Timestamp;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class TransactionServiceImplTest {

  private SessionFactory sessionFactory=mock(SessionFactory.class);

  private AccountService accountService=mock(AccountService.class);

  private StatementService statementService=mock(StatementService.class);

  private TransactionsRepository transactionsRepository = mock(TransactionsRepository.class);

  private Session session = mock(Session.class);

  private Transaction transaction = mock(Transaction.class);

  @InjectMocks
  private TransactionServiceImpl transactionService = new TransactionServiceImpl();

  @Before
  public void setup(){
    doReturn(session).when(sessionFactory).getCurrentSession();
    doReturn(session).when(sessionFactory).openSession();
    doReturn(transaction).when(session).beginTransaction();
    doReturn(true).when(session).isOpen();
  }

  @Test
  public void postTransactionAtAccountSuccess(){
    MoneyTransferRequestDto moneyTransferRequestDto = new MoneyTransferRequestDto("yyy","random", null, new BigDecimal(100), TransactionPlatform.IOS, null, CurrencyType.INR,
        DateTime.now().getMillis());
    String accountNumber = "xxx";
    SubTransactionType subTransactionType = SubTransactionType.MONEY_TRANSFER;

    doAnswer(invocation -> {
      Object[] objects = invocation.getArguments();
      Transactions transactions = (Transactions) objects[0];
      transactions.setTransactionId(1L);
      transactions.setCreatedAt(new Timestamp(DateTime.now().getMillis()));
      return transactions;
    }).when(transactionsRepository).insert(any(Transactions.class));

    doAnswer(invocation ->{
      Object[] objects = invocation.getArguments();
      Transactions transactions = (Transactions) objects[0];
      transactions.setUpdatedAt(new Timestamp(DateTime.now().getMillis()));
      return transactions;
    }).when(transactionsRepository).update(any(Transactions.class));

    Transactions transactions = this.transactionService.postTransactionAtAccount(accountNumber, moneyTransferRequestDto, subTransactionType);
    Assert.assertEquals(Long.valueOf(1), transactions.getTransactionId());
    Assert.assertNotNull(transactions.getCreatedAt());
    Assert.assertNotNull(transactions.getUpdatedAt());
  }

  @Test(expected = RuntimeException.class)
  public void postTransactionAtAccountFailInsert(){
    MoneyTransferRequestDto moneyTransferRequestDto = new MoneyTransferRequestDto("yyy","random", null, new BigDecimal(100), TransactionPlatform.IOS, null, CurrencyType.INR,
        DateTime.now().getMillis());
    String accountNumber = "xxx";
    SubTransactionType subTransactionType = SubTransactionType.MONEY_TRANSFER;
    doThrow(RuntimeException.class).when(transactionsRepository).insert(any());
    this.transactionService.postTransactionAtAccount(accountNumber, moneyTransferRequestDto, subTransactionType);
  }

  @Test(expected = RuntimeException.class)
  public void postTransactionAtAccountFailUpdate(){
    MoneyTransferRequestDto moneyTransferRequestDto = new MoneyTransferRequestDto("yyy","random", null, new BigDecimal(100), TransactionPlatform.IOS, null, CurrencyType.INR,
        DateTime.now().getMillis());
    String accountNumber = "xxx";
    SubTransactionType subTransactionType = SubTransactionType.MONEY_TRANSFER;
    doAnswer(invocation -> {
      Object[] objects = invocation.getArguments();
      Transactions transactions = (Transactions) objects[0];
      transactions.setTransactionId(1L);
      transactions.setCreatedAt(new Timestamp(DateTime.now().getMillis()));
      return transactions;
    }).when(transactionsRepository).insert(any(Transactions.class));
    doThrow(RuntimeException.class).when(transactionsRepository).update(any());
    this.transactionService.postTransactionAtAccount(accountNumber, moneyTransferRequestDto, subTransactionType);
  }

  @Test(expected = AccountNotFoundException.class)
  public void initiateMoneyTransferFailForNullSenderAccount()
      throws DuplicateTransferRequestException, AccountNotFoundException, InsufficientBalanceException, AccountStatusNotValidException {
    this.transactionService.initiateMoneyTransfer("xxx", null);
  }

  @Test(expected = InsufficientBalanceException.class)
  public void initiateMoneyTransferFailForInsufficientBalance()
      throws DuplicateTransferRequestException, AccountNotFoundException, InsufficientBalanceException, AccountStatusNotValidException {
    Accounts account = new Accounts("xxx",123L,new BigDecimal(0), new Timestamp(DateTime.now().getMillis()), null, null,AccountStatus.ACTIVE);
    MoneyTransferRequestDto moneyTransferRequestDto = new MoneyTransferRequestDto("yyy","random", null, new BigDecimal(100), TransactionPlatform.IOS, null, CurrencyType.INR,
        DateTime.now().getMillis());
    doReturn(account).when(accountService).getAccountByAccountNumber("xxx");
    lenient().doReturn(account).when(session).get(Accounts.class, "xxx");
    this.transactionService.initiateMoneyTransfer("xxx", moneyTransferRequestDto);
  }

  @Test(expected = AccountStatusNotValidException.class)
  public void initiateMoneyTransferFailForAccountStatusNotValide()
      throws DuplicateTransferRequestException, AccountNotFoundException, InsufficientBalanceException, AccountStatusNotValidException {
    Accounts account = new Accounts("xxx",123L,new BigDecimal(0), new Timestamp(DateTime.now().getMillis()), null, null,AccountStatus.DEACTIVE);
    MoneyTransferRequestDto moneyTransferRequestDto = new MoneyTransferRequestDto("yyy","random", null, new BigDecimal(100), TransactionPlatform.IOS, null, CurrencyType.INR,
        DateTime.now().getMillis());
    doReturn(account).when(accountService).getAccountByAccountNumber("xxx");
    lenient().doReturn(account).when(session).get(Accounts.class, "xxx");
    this.transactionService.initiateMoneyTransfer("xxx", moneyTransferRequestDto);
  }

  @Test(expected = AccountNotFoundException.class)
  public void initiateMoneyTransferFailForNullReceiverAccount()
      throws DuplicateTransferRequestException, AccountNotFoundException, InsufficientBalanceException, AccountStatusNotValidException {
    Accounts senderAccount = new Accounts("xxx",123L,new BigDecimal(100), new Timestamp(DateTime.now().getMillis()), null, null,AccountStatus.ACTIVE);
    MoneyTransferRequestDto moneyTransferRequestDto = new MoneyTransferRequestDto("yyy","random", null, new BigDecimal(100), TransactionPlatform.IOS, null, CurrencyType.INR,
        DateTime.now().getMillis());

    doReturn(senderAccount).when(accountService).getAccountByAccountNumber("xxx");
    lenient().doReturn(senderAccount).when(session).get(Accounts.class, "xxx");

    this.transactionService.initiateMoneyTransfer("xxx", moneyTransferRequestDto);
  }

  @Test(expected = AccountStatusNotValidException.class)
  public void initiateMoneyTransferFailForAccountStatusNotValidForReceiverAccount()
      throws DuplicateTransferRequestException, AccountNotFoundException, InsufficientBalanceException, AccountStatusNotValidException {
    Accounts senderAccount = new Accounts("xxx",123L,new BigDecimal(100), new Timestamp(DateTime.now().getMillis()), null, null,AccountStatus.ACTIVE);
    Accounts receiverAccount = new Accounts("xxx",123L,new BigDecimal(100), new Timestamp(DateTime.now().getMillis()), null, null,AccountStatus.DEACTIVE);
    MoneyTransferRequestDto moneyTransferRequestDto = new MoneyTransferRequestDto("yyy","random", null, new BigDecimal(100), TransactionPlatform.IOS, null, CurrencyType.INR,
        DateTime.now().getMillis());

    doReturn(senderAccount).when(accountService).getAccountByAccountNumber("xxx");
    lenient().doReturn(senderAccount).when(session).get(Accounts.class, "xxx");
    lenient().doReturn(receiverAccount).when(session).get(Accounts.class, "yyy");
    doReturn(receiverAccount).when(accountService).getAccountByAccountNumber("yyy");

    this.transactionService.initiateMoneyTransfer("xxx", moneyTransferRequestDto);
  }

  @Test(expected =  RuntimeException.class)
  public void initiateMoneyTransferFailForAccountLock()
      throws DuplicateTransferRequestException, AccountNotFoundException, InsufficientBalanceException, AccountStatusNotValidException {
    Accounts senderAccount = new Accounts("xxx",123L,new BigDecimal(100), new Timestamp(DateTime.now().getMillis()), null, null,AccountStatus.ACTIVE);
    Accounts receiverAccount = new Accounts("xxx",123L,new BigDecimal(100), new Timestamp(DateTime.now().getMillis()), null, null,AccountStatus.ACTIVE);
    MoneyTransferRequestDto moneyTransferRequestDto = new MoneyTransferRequestDto("yyy","random", null, new BigDecimal(100), TransactionPlatform.IOS, null, CurrencyType.INR,
        DateTime.now().getMillis());

    doReturn(senderAccount).when(accountService).getAccountByAccountNumber("xxx");
    lenient().doReturn(senderAccount).when(session).get(Accounts.class, "xxx");
    lenient().doReturn(receiverAccount).when(session).get(Accounts.class, "yyy");
    doReturn(receiverAccount).when(accountService).getAccountByAccountNumber("yyy");

    this.transactionService.initiateMoneyTransfer("xxx", moneyTransferRequestDto);
  }

  @Test(expected =  RuntimeException.class)
  public void initiateMoneyTransferFailForUnableToPostDebit()
      throws DuplicateTransferRequestException, AccountNotFoundException, InsufficientBalanceException, AccountStatusNotValidException {
    Accounts senderAccount = new Accounts("xxx",123L,new BigDecimal(100), new Timestamp(DateTime.now().getMillis()), null, null,AccountStatus.ACTIVE);
    Accounts receiverAccount = new Accounts("xxx",123L,new BigDecimal(100), new Timestamp(DateTime.now().getMillis()), null, null,AccountStatus.ACTIVE);
    MoneyTransferRequestDto moneyTransferRequestDto = new MoneyTransferRequestDto("yyy","random", null, new BigDecimal(100), TransactionPlatform.IOS, null, CurrencyType.INR,
        DateTime.now().getMillis());

    doReturn(senderAccount).when(accountService).getAccountByAccountNumber("xxx");
    lenient().doReturn(senderAccount).when(session).get(Accounts.class, "xxx");
    lenient().doReturn(receiverAccount).when(session).get(Accounts.class, "yyy");
    doReturn(receiverAccount).when(accountService).getAccountByAccountNumber("yyy");
    doReturn(senderAccount).when(accountService).getAccountByAccountNumberWithLock("xxx");
    doThrow(RuntimeException.class).when(transactionsRepository).insert(any());
    this.transactionService.initiateMoneyTransfer("xxx", moneyTransferRequestDto);
  }

  @Test(expected =  RuntimeException.class)
  public void initiateMoneyTransferFailForAccountUpdate()
      throws DuplicateTransferRequestException, AccountNotFoundException, InsufficientBalanceException, AccountStatusNotValidException {
    Accounts senderAccount = new Accounts("xxx",123L,new BigDecimal(100), new Timestamp(DateTime.now().getMillis()), null, null,AccountStatus.ACTIVE);
    Accounts receiverAccount = new Accounts("xxx",123L,new BigDecimal(100), new Timestamp(DateTime.now().getMillis()), null, null,AccountStatus.ACTIVE);
    MoneyTransferRequestDto moneyTransferRequestDto = new MoneyTransferRequestDto("yyy","random", null, new BigDecimal(100), TransactionPlatform.IOS, null, CurrencyType.INR,
        DateTime.now().getMillis());

    doReturn(senderAccount).when(accountService).getAccountByAccountNumber("xxx");
    lenient().doReturn(senderAccount).when(session).get(Accounts.class, "xxx");
    lenient().doReturn(receiverAccount).when(session).get(Accounts.class, "yyy");
    doReturn(receiverAccount).when(accountService).getAccountByAccountNumber("yyy");
    doReturn(senderAccount).when(accountService).getAccountByAccountNumberWithLock("xxx");
    doAnswer(invocation -> {
      Object[] objects = invocation.getArguments();
      Transactions transactions = (Transactions) objects[0];
      transactions.setTransactionId(1L);
      transactions.setCreatedAt(new Timestamp(DateTime.now().getMillis()));
      return transactions;
    }).when(transactionsRepository).insert(any(Transactions.class));

    doAnswer(invocation ->{
      Object[] objects = invocation.getArguments();
      Transactions transactions = (Transactions) objects[0];
      transactions.setUpdatedAt(new Timestamp(DateTime.now().getMillis()));
      return transactions;
    }).when(transactionsRepository).update(any(Transactions.class));

    doThrow(RuntimeException.class).when(accountService).reduceBalanceOnSuccessfulTransaction(any(Accounts.class), any(BigDecimal.class));
    this.transactionService.initiateMoneyTransfer("xxx", moneyTransferRequestDto);
  }

  @Test(expected =  RuntimeException.class)
  public void initiateMoneyTransferFailForReceiverAccountLock()
      throws DuplicateTransferRequestException, AccountNotFoundException, InsufficientBalanceException, AccountStatusNotValidException {
    Accounts senderAccount = new Accounts("xxx",123L,new BigDecimal(100), new Timestamp(DateTime.now().getMillis()), null, null,AccountStatus.ACTIVE);
    Accounts receiverAccount = new Accounts("xxx",123L,new BigDecimal(100), new Timestamp(DateTime.now().getMillis()), null, null,AccountStatus.ACTIVE);
    MoneyTransferRequestDto moneyTransferRequestDto = new MoneyTransferRequestDto("yyy","random", null, new BigDecimal(100), TransactionPlatform.IOS, null, CurrencyType.INR,
        DateTime.now().getMillis());

    doReturn(senderAccount).when(accountService).getAccountByAccountNumber("xxx");
    lenient().doReturn(senderAccount).when(session).get(Accounts.class, "xxx");
    lenient().doReturn(receiverAccount).when(session).get(Accounts.class, "yyy");
    doReturn(receiverAccount).when(accountService).getAccountByAccountNumber("yyy");
    doReturn(senderAccount).when(accountService).getAccountByAccountNumberWithLock("xxx");
    doAnswer(invocation -> {
      Object[] objects = invocation.getArguments();
      Transactions transactions = (Transactions) objects[0];
      transactions.setTransactionId(1L);
      transactions.setCreatedAt(new Timestamp(DateTime.now().getMillis()));
      return transactions;
    }).when(transactionsRepository).insert(any(Transactions.class));

    doAnswer(invocation ->{
      Object[] objects = invocation.getArguments();
      Transactions transactions = (Transactions) objects[0];
      transactions.setUpdatedAt(new Timestamp(DateTime.now().getMillis()));
      return transactions;
    }).when(transactionsRepository).update(any(Transactions.class));

    doNothing().when(accountService).reduceBalanceOnSuccessfulTransaction(any(Accounts.class), any(BigDecimal.class));

    this.transactionService.initiateMoneyTransfer("xxx", moneyTransferRequestDto);
  }

  @Test
  public void initiateMoneyTransferSuccess()
      throws DuplicateTransferRequestException, AccountNotFoundException, InsufficientBalanceException, AccountStatusNotValidException {
    Accounts senderAccount = new Accounts("xxx",123L,new BigDecimal(100), new Timestamp(DateTime.now().getMillis()), null, null,AccountStatus.ACTIVE);
    Accounts receiverAccount = new Accounts("xxx",123L,new BigDecimal(100), new Timestamp(DateTime.now().getMillis()), null, null,AccountStatus.ACTIVE);
    MoneyTransferRequestDto moneyTransferRequestDto = new MoneyTransferRequestDto("yyy","random", null, new BigDecimal(100), TransactionPlatform.IOS, null, CurrencyType.INR,
        DateTime.now().getMillis());
    Transactions credit = new Transactions(null, moneyTransferRequestDto.getPaymentGatewayTransactionId(),moneyTransferRequestDto.getReceiverAccountNumber(), null, SubTransactionType.MONEY_TRANSFER, TransactionStatus.SUCCESS, moneyTransferRequestDto.getAmount(), moneyTransferRequestDto.getOriginIP(), moneyTransferRequestDto.getComment(), null, null, "{\"currency\":\"INR\",\"transactionPlatform\":\"IOS\",\"receiverAccountNumber\":\"yyy\",\"senderAccountNumber\":\"xxx\"}");
    Transactions debit = new Transactions(null, moneyTransferRequestDto.getPaymentGatewayTransactionId(),senderAccount.getAccountNumber(), null, SubTransactionType.MONEY_TRANSFER, TransactionStatus.SUCCESS, moneyTransferRequestDto.getAmount(), moneyTransferRequestDto.getOriginIP(), moneyTransferRequestDto.getComment(), null, null, "{\"currency\":\"INR\",\"transactionPlatform\":\"IOS\",\"receiverAccountNumber\":\"yyy\",\"senderAccountNumber\":\"xxx\"}");

    doReturn(senderAccount).when(accountService).getAccountByAccountNumber("xxx");
    lenient().doReturn(senderAccount).when(session).get(Accounts.class, "xxx");
    lenient().doReturn(receiverAccount).when(session).get(Accounts.class, "yyy");
    doReturn(receiverAccount).when(accountService).getAccountByAccountNumber("yyy");
    doReturn(senderAccount).when(accountService).getAccountByAccountNumberWithLock("xxx");
    doAnswer(invocation -> {
      Object[] objects = invocation.getArguments();
      Transactions transactions = (Transactions) objects[0];
      transactions.setTransactionId(1L);
      transactions.setCreatedAt(new Timestamp(DateTime.now().getMillis()));
      return transactions;
    }).when(transactionsRepository).insert(any(Transactions.class));

    doAnswer(invocation ->{
      Object[] objects = invocation.getArguments();
      Transactions transactions = (Transactions) objects[0];
      debit.setExternalReferenceId(transactions.getExternalReferenceId());
      transactions.setUpdatedAt(new Timestamp(DateTime.now().getMillis()));
      return transactions;
    }).when(transactionsRepository).update(any(Transactions.class));

    doNothing().when(accountService).reduceBalanceOnSuccessfulTransaction(any(Accounts.class), any(BigDecimal.class));
    doReturn(senderAccount).when(accountService).getAccountByAccountNumberWithLock("xxx");
    doReturn(receiverAccount).when(accountService).getAccountByAccountNumberWithLock("yyy");

    MoneyTransferResponseDto moneyTransferResponseDto = this.transactionService.initiateMoneyTransfer("xxx", moneyTransferRequestDto);
    Assert.assertEquals(moneyTransferRequestDto.getPaymentGatewayTransactionId(), moneyTransferResponseDto.getPaymentGatewayTransactionId());
    Assert.assertEquals(debit.getStatus(),moneyTransferResponseDto.getStatus());
  }

}

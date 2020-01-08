# Sample Money Transfer APIs

This sample project is based on Dropwizard + Guice + Hibernate + H2 database.

It demonstrates how a transaction system should work for money transfer. 

##Data Model
We have following 4 tables which stores the minimum required data
1) **Users** - Data related to user like first name, last name, email, mobileNumber is stored here.
2) **Accounts** - Data related to user' account, balance, account status is stored.
3) **Statement** - This table is used to server user's passbook request. Basically a transaction can be of 2 type i.e. `Debit` or `Credit`. We are storing the type of transaction, status of transaction, comments attached to it and reference id of this transaction.
4) **Transactions** - This table will contain all data related to a transaction like the sub-type of transaction i.e. `MONEY_TRANSFER`, `CASHBACK`, `REFUND`, `TRANSACTION`, status of transaction, externalReferenceId which will be communicated to the end user, uniqiueTransactionId for the transaction which acts as unique as well as joining dataset for money transfer, IP from which transaction request was made, platform and other data.

##API
We have following 3 controllers in the system
1) **Test** - This controller is used to setup two dummy accounts in the system, we can have one more controller which takes input from user and crate a account in the system but for the sake of test we will use this.
2) **Transact** - Currently this controller has only 1 method which is `initiateMoneyTransfer` but in future it will contain all method related to all types of transaction.
3) **Statement** - This controller serves users's statement request.

##CURL

To setup dummy account<br>
`curl -X POST http://localhost:8080/dbug/account`
<br>It will response two accountNumbers in the response

To initiate money transfer<br>
 `curl -X POST http://localhost:8080/api/v1/transact/transfer -H 'Content-Type: application/json' -H 'X-ACCOUNT-NUMBER: <sender-account-number>' -d '{"paymentGatewayTransactionId":"9142020010823CDBBBA1","receiverAccountNumber":"<receiver-account-number>","comment":"Grocery shopping","amount":150.00,"transactionPlatform":"ANDROID","originIP":"13.214.76.67","currency":"INR","timestamp":1577898323000  }'`
 
 To check user's statement<br>
 `curl -X GET \
    'http://localhost:8080/api/v1/user/statement?=' \
    -H 'X-ACCOUNT-NUMBER: xxx' `
    

 
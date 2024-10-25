package net.uniloftsky.markant.bank.biz;

import net.uniloftsky.markant.bank.biz.persistence.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static net.uniloftsky.markant.bank.biz.BankServiceImpl.INITIAL_BALANCE;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
public class BankServiceImplTest {

    @Mock
    private BankPersistenceService persistenceService;

    @Mock
    private Clock clock;

    @Spy
    @InjectMocks
    private BankServiceImpl bankService;

    /**
     * Dummy number
     */
    private long number;

    /**
     * Dummy account number
     */
    private AccountNumber accountNumber;

    @BeforeEach
    void setUp() {
        number = 1234567890L;
        accountNumber = AccountNumber.of(number);
    }

    @Test
    public void testGetAccount() throws AccountNotFoundException, AccountNotFoundPersistenceServiceException {

        // given
        BigDecimal balance = new BigDecimal("100");

        // mocking account object from persistence service
        AccountEntity entity = new AccountEntity();
        entity.setNumber(number);
        entity.setBalance(balance.toPlainString());
        given(persistenceService.getAccount(number)).willReturn(Optional.of(entity));

        // when
        BankAccount result = bankService.getAccount(accountNumber);

        // then
        assertNotNull(result);
        assertEquals(accountNumber, result.getNumber());
        assertEquals(0, balance.compareTo(result.getBalance()));
    }

    @Test
    public void testGetAccountNotFound() throws AccountNotFoundPersistenceServiceException {

        // given
        // mocking persistence service behaviour to return empty optional of account entity
        given(persistenceService.getAccount(number)).willReturn(Optional.empty());

        try {

            // when
            bankService.getAccount(accountNumber);
        } catch (AccountNotFoundException ex) {

            // then
            assertNotNull(ex);
        }
    }

    @Test
    public void testWithdraw() throws InsufficientBalanceException, AccountNotFoundException {

        // given
        BigDecimal withdrawalAmount = new BigDecimal("100.49");
        BigDecimal accountBalance = new BigDecimal("200.65");
        BigDecimal afterWithdrawal = accountBalance.subtract(withdrawalAmount);

        // creating the mock of account entity with balance of "accountBalance"
        AccountEntity accountEntity = new AccountEntity();
        accountEntity.setNumber(number);
        accountEntity.setBalance(accountBalance.toPlainString());
        given(persistenceService.getAccount(number)).willReturn(Optional.of(accountEntity));

        // mocking the clock to return a predefined timestamp for the transaction
        long transactionTimestamp = System.currentTimeMillis();
        mockClockInstant(transactionTimestamp);

        // mocking the persistence layer to return an updated account with the new balance
        mockAccountWithUpdatedBalance(accountEntity, afterWithdrawal, transactionTimestamp);

        // when
        BankAccount result = bankService.withdraw(accountNumber, withdrawalAmount);

        // then
        assertNotNull(result);
        assertEquals(accountNumber, result.getNumber());
        assertEquals(0, afterWithdrawal.compareTo(result.getBalance()));
    }

    @Test
    public void testWithdrawNotEnoughMoneyException() throws AccountNotFoundException {

        // given
        // withdrawal amount is greater than the account balance on purpose to invoke the exception
        BigDecimal withdrawalAmount = new BigDecimal("500.49");
        BigDecimal accountBalance = new BigDecimal("200.65");

        // creating the mock of account entity with balance of "accountBalance"
        AccountEntity accountEntity = new AccountEntity();
        accountEntity.setNumber(number);
        accountEntity.setBalance(accountBalance.toPlainString());
        given(persistenceService.getAccount(number)).willReturn(Optional.of(accountEntity));

        try {

            // when
            bankService.withdraw(accountNumber, withdrawalAmount);
        } catch (InsufficientBalanceException ex) {

            // then
            assertNotNull(ex);
        }
    }

    @Test
    public void testDeposit() {

        // given
        BigDecimal depositAmount = new BigDecimal("100.49");
        BigDecimal accountBalance = new BigDecimal("200.65");
        BigDecimal afterDeposit = accountBalance.add(depositAmount);

        // creating the mock of account entity with balance of "accountBalance"
        AccountEntity accountEntity = new AccountEntity();
        accountEntity.setNumber(number);
        accountEntity.setBalance(accountBalance.toPlainString());
        doReturn(accountEntity).when(bankService).getOrCreateAccountEntity(accountNumber);

        // mocking the clock to return predefined timestamp
        long transactionTimestamp = System.currentTimeMillis();
        mockClockInstant(transactionTimestamp);

        // mocking the persistence layer to return an updated account with the new balance
        mockAccountWithUpdatedBalance(accountEntity, afterDeposit, transactionTimestamp);

        // when
        BankAccount result = bankService.deposit(accountNumber, depositAmount);

        // then
        assertNotNull(result);
        assertEquals(accountNumber, result.getNumber());
        assertEquals(0, afterDeposit.compareTo(result.getBalance()));
    }

    @Test
    public void testValidateTransactionParametersAccountNumberIsNull() {

        // given
        BigDecimal amount = new BigDecimal("100");

        try {

            // when
            bankService.validateTransactionParameters(null, amount);
        } catch (IllegalArgumentException ex) {

            // then
            assertNotNull(ex);
        }
    }

    @Test
    public void testValidateTransactionParametersAmountIsNull() {
        try {

            // when
            bankService.validateTransactionParameters(accountNumber, null);
        } catch (IllegalArgumentException ex) {

            // then
            assertNotNull(ex);
        }
    }

    @Test
    public void testValidateTransactionParametersAmountIsInvalid() {

        // given
        BigDecimal amount = new BigDecimal("-100");

        try {

            // when
            bankService.validateTransactionParameters(accountNumber, amount);
        } catch (IllegalArgumentException ex) {

            // then
            assertNotNull(ex);
        }
    }

    @Test
    public void testGetOrCreateAccountEntityExists() {

        // given
        // creating a mock of account entity from persistence
        AccountEntity entity = new AccountEntity();
        entity.setNumber(number);

        String balance = "100";
        entity.setBalance(balance);
        given(persistenceService.getAccount(number)).willReturn(Optional.of(entity));

        // when
        AccountEntity result = bankService.getOrCreateAccountEntity(accountNumber);

        // then
        assertNotNull(result);
        assertEquals(number, result.getNumber());
        assertEquals(balance, result.getBalance());
    }

    @Test
    public void testGetOrCreateAccountEntityNotExist() {

        // given
        // throw exception to simulate that requested account doesn't exist in persistence
        // return empty account entity optional from persistence
        given(persistenceService.getAccount(number)).willReturn(Optional.empty());

        // mocking the clock to return predefined timestamp for account creation
        long accountCreationTimestamp = System.currentTimeMillis();
        mockClockInstant(accountCreationTimestamp);

        // mocking persistent layer to return created account entity
        AccountEntity entity = new AccountEntity();
        entity.setNumber(number);
        String balance = "100";
        entity.setBalance(balance);
        given(persistenceService.createAccount(number, INITIAL_BALANCE, accountCreationTimestamp)).willReturn(entity);

        // when
        AccountEntity result = bankService.getOrCreateAccountEntity(accountNumber);

        // then
        assertNotNull(result);
        assertEquals(number, result.getNumber());
        assertEquals(balance, result.getBalance());
    }

    @Test
    public void testListWithdrawals() {

        // given
        // mock bank service to return account entity
        doReturn(new AccountEntity()).when(bankService).getAccountEntity(accountNumber);

        WithdrawTransactionEntity firstEntity = new WithdrawTransactionEntity();
        firstEntity.setId(UUID.randomUUID());
        firstEntity.setAmount("100");
        firstEntity.setTimestamp(123456L);
        firstEntity.setAccountNumber(number);

        WithdrawTransactionEntity secondEntity = new WithdrawTransactionEntity();
        secondEntity.setId(UUID.randomUUID());
        secondEntity.setAmount("200");
        secondEntity.setTimestamp(123457L);
        secondEntity.setAccountNumber(number);

        // mock persistence layer to return list of withdrawals entities
        List<WithdrawTransactionEntity> entities = List.of(firstEntity, secondEntity);
        given(persistenceService.listWithdrawals(number)).willReturn(entities);

        // when
        List<WithdrawTransaction> result = bankService.listWithdrawals(accountNumber);

        // then
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(entities.size(), result.size());

        // testing that withdrawal transactions are ordered correctly and properties are mapped accurately
        // the first transaction should have the properties from the second entity (most recent timestamp)
        WithdrawTransaction firstTransaction = result.getFirst();
        assertEquals(secondEntity.getId(), firstTransaction.getId().getId());
        assertEquals(secondEntity.getAmount(), firstTransaction.getAmount().toPlainString());
        assertEquals(secondEntity.getTimestamp(), firstTransaction.getTimestamp().toEpochMilli());
        assertEquals(secondEntity.getAccountNumber(), firstTransaction.getAccountNumber().getNumber());

        // the second transaction should have properties from the first entity
        WithdrawTransaction secondTransaction = result.getLast();
        assertEquals(firstEntity.getId(), secondTransaction.getId().getId());
        assertEquals(firstEntity.getAmount(), secondTransaction.getAmount().toPlainString());
        assertEquals(firstEntity.getTimestamp(), secondTransaction.getTimestamp().toEpochMilli());
        assertEquals(firstEntity.getAccountNumber(), secondTransaction.getAccountNumber().getNumber());
    }

    @Test
    public void testListDeposits() {

        // given
        // mock bank service to return account entity
        doReturn(new AccountEntity()).when(bankService).getAccountEntity(accountNumber);

        DepositTransactionEntity firstEntity = new DepositTransactionEntity();
        firstEntity.setId(UUID.randomUUID());
        firstEntity.setAmount("100");
        firstEntity.setTimestamp(123456L);
        firstEntity.setAccountNumber(number);

        DepositTransactionEntity secondEntity = new DepositTransactionEntity();
        secondEntity.setId(UUID.randomUUID());
        secondEntity.setAmount("200");
        secondEntity.setTimestamp(123457L);
        secondEntity.setAccountNumber(number);

        // mock persistence layer to return list of withdrawals entities
        List<DepositTransactionEntity> entities = List.of(firstEntity, secondEntity);
        given(persistenceService.listDeposits(number)).willReturn(entities);

        // when
        List<DepositTransaction> result = bankService.listDeposits(accountNumber);

        // then
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(entities.size(), result.size());

        // testing that deposit transactions are ordered correctly and properties are mapped accurately
        // the first transaction should have the properties from the second entity (most recent timestamp)
        DepositTransaction firstTransaction = result.getFirst();
        assertEquals(secondEntity.getId(), firstTransaction.getId().getId());
        assertEquals(secondEntity.getAmount(), firstTransaction.getAmount().toPlainString());
        assertEquals(secondEntity.getTimestamp(), firstTransaction.getTimestamp().toEpochMilli());
        assertEquals(secondEntity.getAccountNumber(), firstTransaction.getAccountNumber().getNumber());

        // the second transaction should have properties from the first entity
        DepositTransaction secondTransaction = result.getLast();
        assertEquals(firstEntity.getId(), secondTransaction.getId().getId());
        assertEquals(firstEntity.getAmount(), secondTransaction.getAmount().toPlainString());
        assertEquals(firstEntity.getTimestamp(), secondTransaction.getTimestamp().toEpochMilli());
        assertEquals(firstEntity.getAccountNumber(), secondTransaction.getAccountNumber().getNumber());
    }

    @Test
    public void testListTransactions() {

        // given
        // mock bank service to return deposits
        TransactionId depositId = TransactionId.generateNew();
        long depositTimestamp = 123456L;
        BigDecimal depositAmount = new BigDecimal("100.50");
        DepositTransaction deposit = new DepositTransaction(depositId, accountNumber, depositAmount, Instant.ofEpochMilli(depositTimestamp));
        List<DepositTransaction> deposits = List.of(deposit);
        doReturn(deposits).when(bankService).listDeposits(accountNumber);

        // mock bank service to return withdrawals
        TransactionId withdrawalId = TransactionId.generateNew();
        long withdrawalTimestamp = 123457L;
        BigDecimal withdrawalAmount = new BigDecimal("200.50");
        WithdrawTransaction withdrawal = new WithdrawTransaction(withdrawalId, accountNumber, withdrawalAmount, Instant.ofEpochMilli(withdrawalTimestamp));
        List<WithdrawTransaction> withdrawals = List.of(withdrawal);
        doReturn(withdrawals).when(bankService).listWithdrawals(accountNumber);

        // when
        List<BankTransaction> result = bankService.listTransactions(accountNumber);

        // then
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(withdrawals.size() + deposits.size(), result.size());

        // testing that transactions are ordered by timestamp in descending order
        // The first transaction in result list must be withdrawal (most recent timestamp)
        TransactionId firstTransactionInResultId = result.getFirst().getId();
        assertEquals(withdrawalId, firstTransactionInResultId);
    }

    /**
     * Method to mock the clock to return fixed timestamp
     *
     * @param timestamp timestamp to return
     */
    private void mockClockInstant(long timestamp) {
        Instant instant = mock(Instant.class);
        given(clock.instant()).willReturn(instant);
        given(instant.toEpochMilli()).willReturn(timestamp);
    }

    /**
     * Method to mock the persistence service to simulate balance update
     *
     * @param entity         account entity to update
     * @param updatedBalance updated balance
     * @param timestamp      timestamp of update
     */
    private void mockAccountWithUpdatedBalance(AccountEntity entity, BigDecimal updatedBalance, long timestamp) {
        AccountEntity updatedAccountEntity = new AccountEntity();
        updatedAccountEntity.setNumber(number);
        updatedAccountEntity.setBalance(updatedBalance.toPlainString());
        given(persistenceService.updateAccountBalance(entity, updatedBalance.toPlainString(), timestamp)).willReturn(updatedAccountEntity);
    }

}

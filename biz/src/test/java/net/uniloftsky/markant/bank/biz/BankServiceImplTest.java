package net.uniloftsky.markant.bank.biz;

import net.uniloftsky.markant.bank.biz.persistence.AccountEntity;
import net.uniloftsky.markant.bank.biz.persistence.AccountNotFoundPersistenceServiceException;
import net.uniloftsky.markant.bank.biz.persistence.BankPersistenceService;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BankServiceImplTest {

    @Mock
    private BankPersistenceService persistenceService;

    @Mock
    private Clock clock;

    @Spy
    @InjectMocks
    private BankServiceImpl bankService;

    private long number;
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
        given(persistenceService.getAccount(number)).willReturn(entity);

        // when
        BankAccount result = bankService.getAccount(accountNumber);

        // then
        assertNotNull(result);
        assertEquals(accountNumber, result.getId());
        assertEquals(0, balance.compareTo(result.getBalance()));
    }

    @Test
    public void testGetAccountNotFound() throws AccountNotFoundPersistenceServiceException {

        // given
        // mocking persistence service behaviour to throw AccountNotFoundPersistenceServiceException
        doThrow(new AccountNotFoundPersistenceServiceException()).when(persistenceService).getAccount(number);

        try {

            // when
            bankService.getAccount(accountNumber);
        } catch (AccountNotFoundException ex) {

            // then
            assertNotNull(ex);
        }
    }

    @Test
    public void testWithdraw() throws NotEnoughMoneyException, AccountNotFoundException {

        // given
        BigDecimal withdrawalAmount = new BigDecimal("100.49");
        BigDecimal accountBalance = new BigDecimal("200.65");
        BigDecimal afterWithdrawal = accountBalance.subtract(withdrawalAmount);

        // creating the mock of account with balance of "accountBalance"
        BankAccount account = new BankAccount(accountNumber, accountBalance);
        doReturn(account).when(bankService).getAccount(accountNumber);

        // mocking the clock to return a predefined timestamp for the transaction
        long transactionTimestamp = System.currentTimeMillis();
        mockClockInstant(transactionTimestamp);

        // mocking the persistence layer to return an updated account with the new balance
        mockAccountWithUpdatedBalance(number, afterWithdrawal, transactionTimestamp);

        // when
        BankAccount result = bankService.withdraw(accountNumber, withdrawalAmount);

        // then
        assertNotNull(result);
        assertEquals(accountNumber, result.getId());
        assertEquals(0, afterWithdrawal.compareTo(result.getBalance()));
    }

    @Test
    public void testWithdrawNotEnoughMoneyException() throws AccountNotFoundException {

        // given
        // withdrawal amount is greater than the account balance on purpose to invoke the exception
        BigDecimal withdrawalAmount = new BigDecimal("500.49");
        BigDecimal accountBalance = new BigDecimal("200.65");

        // creating the mock of account with balance of "accountBalance"
        BankAccount account = new BankAccount(accountNumber, accountBalance);
        doReturn(account).when(bankService).getAccount(accountNumber);

        try {

            // when
            bankService.withdraw(accountNumber, withdrawalAmount);
        } catch (NotEnoughMoneyException ex) {

            // then
            assertNotNull(ex);
        }
    }

    @Test
    public void testDeposit() throws AccountNotFoundException {

        // given
        BigDecimal depositAmount = new BigDecimal("100.49");
        BigDecimal accountBalance = new BigDecimal("200.65");
        BigDecimal afterDeposit = accountBalance.add(depositAmount);

        // creating the mock of account with balance of "accountBalance"
        BankAccount account = new BankAccount(accountNumber, accountBalance);
        doReturn(account).when(bankService).getAccount(accountNumber);

        // mocking the clock to return predefined timestamp
        long transactionTimestamp = System.currentTimeMillis();
        mockClockInstant(transactionTimestamp);

        // mocking the persistence layer to return an updated account with the new balance
        mockAccountWithUpdatedBalance(number, afterDeposit, transactionTimestamp);

        // when
        BankAccount result = bankService.deposit(accountNumber, depositAmount);

        // then
        assertNotNull(result);
        assertEquals(accountNumber, result.getId());
        assertEquals(0, afterDeposit.compareTo(result.getBalance()));
    }

    @Test
    public void testDepositUnknownAccount() throws AccountNotFoundException {

        // given
        // throwing the exception to simulate the behaviour when account by specific number cannot be found
        doThrow(new AccountNotFoundException()).when(bankService).getAccount(accountNumber);

        // mocking the clock to return predefined timestamp
        long transactionTimestamp = System.currentTimeMillis();
        mockClockInstant(transactionTimestamp);

        // mocking the account creation with predefined balance
        BigDecimal accountBalance = new BigDecimal("0");
        BankAccount createdAccount = new BankAccount(accountNumber, accountBalance);
        doReturn(createdAccount).when(bankService).createAccount(accountNumber);

        BigDecimal depositAmount = new BigDecimal("100.49");
        BigDecimal afterDeposit = accountBalance.add(depositAmount);

        // mocking the persistence layer to return an updated account with the new balance
        mockAccountWithUpdatedBalance(number, afterDeposit, transactionTimestamp);

        // when
        BankAccount result = bankService.deposit(accountNumber, depositAmount);

        // then
        assertNotNull(result);
        assertEquals(accountNumber, result.getId());
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
    public void testCreateAccount() {

        // given
        long creationTimestamp = System.currentTimeMillis();
        mockClockInstant(creationTimestamp);

        AccountEntity createdEntity = new AccountEntity();
        createdEntity.setNumber(number);
        createdEntity.setCreatedAt(creationTimestamp);
        createdEntity.setBalance("100");
        given(persistenceService.createAccount(number, creationTimestamp)).willReturn(createdEntity);

        // when
        BankAccount result = bankService.createAccount(accountNumber);

        // then
        assertNotNull(result);
        assertEquals(accountNumber, result.getId());
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
     * @param number         account number
     * @param updatedBalance updated balance
     * @param timestamp      timestamp of update
     */
    private void mockAccountWithUpdatedBalance(long number, BigDecimal updatedBalance, long timestamp) {
        AccountEntity updatedAccountEntity = new AccountEntity();
        updatedAccountEntity.setNumber(number);
        updatedAccountEntity.setBalance(updatedBalance.toPlainString());
        given(persistenceService.updateAccountBalance(number, updatedBalance.toPlainString(), timestamp)).willReturn(updatedAccountEntity);
    }

}

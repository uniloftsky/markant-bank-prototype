package net.uniloftsky.markant.bank.biz;

import net.uniloftsky.markant.bank.biz.persistence.AccountEntity;
import net.uniloftsky.markant.bank.biz.persistence.AccountNotFoundPersistenceServiceException;
import net.uniloftsky.markant.bank.biz.persistence.BankPersistenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Clock;

@Service
public class BankServiceImpl implements BankService {

    /**
     * Length of account number
     */
    private static final int ACCOUNT_NUMBER_LENGTH = 10;

    /**
     * Bank persistence service to work with database
     */
    private BankPersistenceService persistenceService;

    /**
     * Clock instance
     */
    private Clock clock;

    public BankServiceImpl() {
        this.clock = Clock.systemUTC();
    }

    @Override
    @Transactional(readOnly = true)
    public BankAccount getAccount(BankAccount.AccountNumber accountNumber) throws AccountNotFoundException {
        try {
            return map(persistenceService.getAccount(accountNumber.getNumber()));
        } catch (AccountNotFoundPersistenceServiceException notFound) {
            throw new AccountNotFoundException(notFound.getMessage());
        }
    }

    @Override
    @Transactional
    public BankAccount withdraw(BankAccount.AccountNumber accountNumber, BigDecimal amount) throws AccountNotFoundException, NotEnoughMoneyException {
        validateTransactionParameters(accountNumber, amount);

        BankAccount account = getAccount(accountNumber);
        BigDecimal balanceAfterWithdrawal = account.getBalance().subtract(amount);
        if (balanceAfterWithdrawal.compareTo(BigDecimal.ZERO) < 0) {
            throw new NotEnoughMoneyException("withdrawal amount is greater than the current account balance");
        }

        long transactionTimestamp = clock.instant().toEpochMilli();
        AccountEntity updatedAccount = persistenceService.updateAccountBalance(accountNumber.getNumber(), balanceAfterWithdrawal.doubleValue(), transactionTimestamp);
        return map(updatedAccount);
    }

    @Override
    @Transactional
    public BankAccount deposit(BankAccount.AccountNumber accountNumber, BigDecimal amount) {
        validateTransactionParameters(accountNumber, amount);

        BankAccount account;
        try {
            account = getAccount(accountNumber);
        } catch (AccountNotFoundException ex) {

            // create a new account if it cannot be found
            long accountCreationTimestamp = clock.instant().toEpochMilli();
            account = map(persistenceService.createAccount(accountNumber.getNumber(), accountCreationTimestamp));
        }

        BigDecimal balanceAfterDeposit = account.getBalance().add(amount);
        long transactionTimestamp = clock.instant().toEpochMilli();
        AccountEntity updatedAccount = persistenceService.updateAccountBalance(accountNumber.getNumber(), balanceAfterDeposit.doubleValue(), transactionTimestamp);
        return map(updatedAccount);
    }

    private BankAccount map(AccountEntity entity) {
        BankAccount.AccountNumber accountNumber = new BankAccount.AccountNumber(entity.getId());
        BigDecimal balance = BigDecimal.valueOf(entity.getBalance());
        return new BankAccount(accountNumber, balance);
    }

    /**
     * Method to validate transaction parameters
     *
     * @param accountNumber account ID
     * @param amount        transaction amount
     */
    private void validateTransactionParameters(BankAccount.AccountNumber accountNumber, BigDecimal amount) {
        if (accountNumber == null) {
            throw new IllegalArgumentException("accountId cannot be null");
        }
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("transaction amount must be greater than zero");
        }
    }

    private BankAccount createAccount(int accountNumber) {
        long creationTimestamp = clock.instant().toEpochMilli();
        AccountEntity createdAccountEntity = persistenceService.createAccount(accountNumber, creationTimestamp);
        return map(createdAccountEntity);
    }

    private void validateAccountNumber(int accountNumber) {
        String accountNumberString = String.valueOf(accountNumber);

    }

    @Autowired
    public void setPersistenceService(BankPersistenceService persistenceService) {
        this.persistenceService = persistenceService;
    }
}

package net.uniloftsky.markant.bank.biz;

import net.anotheria.idbasedlock.IdBasedLock;
import net.anotheria.idbasedlock.IdBasedLockManager;
import net.anotheria.idbasedlock.SafeIdBasedLockManager;
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
     * Persistence service
     */
    private BankPersistenceService persistenceService;

    /**
     * Clock instance
     */
    private Clock clock;

    /**
     * Lock manager to synchronize transactions within a single account
     */
    private IdBasedLockManager<AccountNumber> accountLockManager;

    public BankServiceImpl() {
        this.clock = Clock.systemUTC();
        this.accountLockManager = new SafeIdBasedLockManager<>();
    }

    @Override
    @Transactional(readOnly = true)
    public BankAccount getAccount(AccountNumber accountNumber) throws AccountNotFoundException {
        try {
            return map(persistenceService.getAccount(accountNumber.getNumber()));
        } catch (AccountNotFoundPersistenceServiceException notFound) {
            throw new AccountNotFoundException(notFound.getMessage());
        }
    }

    @Override
    @Transactional
    public BankAccount withdraw(AccountNumber accountNumber, BigDecimal amount) throws AccountNotFoundException, InsufficientBalanceException {
        validateTransactionParameters(accountNumber, amount);

        IdBasedLock<AccountNumber> lock = accountLockManager.obtainLock(accountNumber);
        lock.lock();
        try {
            AccountEntity accountEntity = persistenceService.getAccount(accountNumber.getNumber());
            BankAccount account = map(accountEntity);
            BigDecimal balanceAfterWithdrawal = account.getBalance().subtract(amount);
            if (balanceAfterWithdrawal.compareTo(BigDecimal.ZERO) < 0) {
                throw new InsufficientBalanceException("withdrawal amount is greater than the current account balance");
            }

            long transactionTimestamp = clock.instant().toEpochMilli();
            AccountEntity updatedAccountEntity = persistenceService.updateAccountBalance(accountEntity, balanceAfterWithdrawal.toPlainString(), transactionTimestamp);
            return map(updatedAccountEntity);
        } catch (AccountNotFoundPersistenceServiceException ex) {
            throw new AccountNotFoundException(ex.getMessage());
        } finally {
            lock.unlock();
        }
    }

    @Override
    @Transactional
    public BankAccount deposit(AccountNumber accountNumber, BigDecimal amount) {
        validateTransactionParameters(accountNumber, amount);

        IdBasedLock<AccountNumber> lock = accountLockManager.obtainLock(accountNumber);
        lock.lock();
        try {
            AccountEntity accountEntity = getOrCreateAccountEntity(accountNumber);
            BankAccount account = map(accountEntity);
            BigDecimal balanceAfterDeposit = account.getBalance().add(amount);
            long transactionTimestamp = clock.instant().toEpochMilli();
            AccountEntity updatedAccountEntity = persistenceService.updateAccountBalance(accountEntity, balanceAfterDeposit.toPlainString(), transactionTimestamp);
            return map(updatedAccountEntity);
        } finally {
            lock.unlock();
        }
    }

    private BankAccount map(AccountEntity entity) {
        AccountNumber accountNumber = AccountNumber.of(entity.getNumber());
        BigDecimal balance = new BigDecimal(entity.getBalance());
        return new BankAccount(accountNumber, balance);
    }

    /**
     * Method to validate transaction parameters
     *
     * @param accountNumber account ID
     * @param amount        transaction amount
     */
    void validateTransactionParameters(AccountNumber accountNumber, BigDecimal amount) {
        if (accountNumber == null) {
            throw new IllegalArgumentException("accountId cannot be null");
        }
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("transaction amount must be greater than zero");
        }
    }

    AccountEntity getOrCreateAccountEntity(AccountNumber accountNumber) {
        try {
            return persistenceService.getAccount(accountNumber.getNumber());
        } catch (AccountNotFoundPersistenceServiceException ex) {

            // If not found, create a new account
            long accountCreationTimestamp = clock.instant().toEpochMilli();
            return persistenceService.createAccount(accountNumber.getNumber(), accountCreationTimestamp);
        }
    }

    @Autowired
    public void setPersistenceService(BankPersistenceService persistenceService) {
        this.persistenceService = persistenceService;
    }
}

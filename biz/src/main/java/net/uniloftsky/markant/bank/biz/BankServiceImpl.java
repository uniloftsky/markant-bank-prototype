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
    public BankAccount getAccount(AccountNumber accountNumber) throws AccountNotFoundException {
        try {
            return map(persistenceService.getAccount(accountNumber.getNumber()));
        } catch (AccountNotFoundPersistenceServiceException notFound) {
            throw new AccountNotFoundException(notFound.getMessage());
        }
    }

    @Override
    @Transactional
    public BankAccount withdraw(AccountNumber accountNumber, BigDecimal amount) throws AccountNotFoundException, NotEnoughMoneyException {
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
    public BankAccount deposit(AccountNumber accountNumber, BigDecimal amount) {
        validateTransactionParameters(accountNumber, amount);

        BankAccount account;
        try {
            account = getAccount(accountNumber);
        } catch (AccountNotFoundException ex) {
            account = createAccount(accountNumber); // create a new account if it cannot be found
        }

        BigDecimal balanceAfterDeposit = account.getBalance().add(amount);
        long transactionTimestamp = clock.instant().toEpochMilli();
        AccountEntity updatedAccount = persistenceService.updateAccountBalance(accountNumber.getNumber(), balanceAfterDeposit.doubleValue(), transactionTimestamp);
        return map(updatedAccount);
    }

    private BankAccount map(AccountEntity entity) {
        AccountNumber accountNumber = AccountNumber.of(entity.getId());
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

    BankAccount createAccount(AccountNumber accountNumber) {
        long creationTimestamp = clock.instant().toEpochMilli();
        AccountEntity createdAccountEntity = persistenceService.createAccount(accountNumber.getNumber(), creationTimestamp);
        return map(createdAccountEntity);
    }

    @Autowired
    public void setPersistenceService(BankPersistenceService persistenceService) {
        this.persistenceService = persistenceService;
    }
}

package net.uniloftsky.markant.bank.biz;

import net.anotheria.idbasedlock.IdBasedLock;
import net.anotheria.idbasedlock.IdBasedLockManager;
import net.anotheria.idbasedlock.SafeIdBasedLockManager;
import net.uniloftsky.markant.bank.biz.persistence.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class BankServiceImpl implements BankService {

    // Default balance value on account creation
    static final String INITIAL_BALANCE = "0";

    /**
     * Persistence service
     */
    private BankPersistenceService persistenceService;

    /**
     * Clock instance
     */
    private Clock clock;

    /**
     * Lock manager to synchronize transactions
     */
    private IdBasedLockManager<AccountNumber> accountLockManager;

    public BankServiceImpl() {
        this.clock = Clock.systemUTC();
        this.accountLockManager = new SafeIdBasedLockManager<>();
    }

    @Override
    @Transactional(readOnly = true)
    public BankAccount getAccount(AccountNumber accountNumber) {
        AccountEntity entity = getAccountEntity(accountNumber);
        return map(entity);
    }

    @Override
    @Transactional
    public BankAccount withdraw(AccountNumber accountNumber, BigDecimal amount) {
        validateTransactionParameters(accountNumber, amount);

        IdBasedLock<AccountNumber> lock = accountLockManager.obtainLock(accountNumber);
        lock.lock();
        try {
            AccountEntity accountEntity = getAccountEntity(accountNumber);
            BankAccount account = map(accountEntity);
            BigDecimal balanceAfterWithdrawal = account.getBalance().subtract(amount);
            if (balanceAfterWithdrawal.compareTo(BigDecimal.ZERO) < 0) {
                throw new InsufficientBalanceException("withdrawal amount is greater than the current account balance");
            }

            // create withdrawal transaction
            long transactionTimestamp = clock.instant().toEpochMilli();
            createWithdrawTransaction(accountNumber, amount, transactionTimestamp);

            // update account balance
            account = updateBalance(accountEntity, balanceAfterWithdrawal, transactionTimestamp);
            return account;
        } finally {
            lock.unlock();
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<WithdrawTransaction> listWithdrawals(AccountNumber accountNumber) {
        getAccountEntity(accountNumber); // get account entity to check if it exists, otherwise an exception will be thrown
        List<WithdrawTransactionEntity> entities = persistenceService.listWithdrawals(accountNumber.getNumber());

        // map persistence layer entities to business layer objects
        List<WithdrawTransaction> result = new ArrayList<>(entities.size());
        for (WithdrawTransactionEntity entity : entities) {
            WithdrawTransaction transaction = map(entity);
            result.add(transaction);
        }

        // sort the result
        result.sort(Comparator.comparing(WithdrawTransaction::getTimestamp).reversed());
        return result;
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

            // create deposit transaction
            long transactionTimestamp = clock.instant().toEpochMilli();
            createDepositTransaction(accountNumber, amount, transactionTimestamp);

            // update account balance
            account = updateBalance(accountEntity, balanceAfterDeposit, transactionTimestamp);
            return account;
        } finally {
            lock.unlock();
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<DepositTransaction> listDeposits(AccountNumber accountNumber) {
        getAccountEntity(accountNumber); // get account entity to check if it exists, otherwise an exception will be thrown
        List<DepositTransactionEntity> entities = persistenceService.listDeposits(accountNumber.getNumber());

        // map persistence layer entities to business layer objects
        List<DepositTransaction> result = new ArrayList<>(entities.size());
        for (DepositTransactionEntity entity : entities) {
            DepositTransaction transaction = map(entity);
            result.add(transaction);
        }

        // sort the result
        result.sort(Comparator.comparing(DepositTransaction::getTimestamp).reversed());
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public List<BankTransaction> listTransactions(AccountNumber accountNumber) {
        List<DepositTransaction> deposits = listDeposits(accountNumber);
        List<WithdrawTransaction> withdrawals = listWithdrawals(accountNumber);
        List<TransferTransaction> transfers = listTransfers(accountNumber);

        List<BankTransaction> result = new ArrayList<>(deposits.size() + withdrawals.size() + transfers.size());
        result.addAll(deposits);
        result.addAll(withdrawals);
        result.addAll(transfers);

        // sorting the combined result list
        result.sort(Comparator.comparing(BankTransaction::getTimestamp).reversed());
        return result;
    }

    @Override
    @Transactional
    public TransferTransaction transfer(AccountNumber fromAccountNumber, AccountNumber toAccountNumber, BigDecimal amount) {
        IdBasedLock<AccountNumber> fromLock = accountLockManager.obtainLock(fromAccountNumber);
        IdBasedLock<AccountNumber> toLock = accountLockManager.obtainLock(toAccountNumber);
        fromLock.lock();
        toLock.lock();
        try {
            long transferTimestamp = clock.instant().toEpochMilli();

            // subtract transfer amount from initiator account and update balance
            AccountEntity fromEntity = getAccountEntity(fromAccountNumber);
            BankAccount fromAccount = map(fromEntity);
            BigDecimal balanceAfterInitiator = fromAccount.getBalance().subtract(amount);
            if (balanceAfterInitiator.compareTo(BigDecimal.ZERO) < 0) {
                throw new InsufficientBalanceException("transfer amount is greater than the current account transfer initiator balance");
            }

            // update balance for initiator account
            updateBalance(fromEntity, balanceAfterInitiator, transferTimestamp);

            // add transfer amount to target account and update balance
            AccountEntity toEntity = getAccountEntity(toAccountNumber);
            BankAccount toAccount = map(toEntity);
            BigDecimal balanceAfterTarget = toAccount.getBalance().add(amount);

            // update balance for target account
            updateBalance(toEntity, balanceAfterTarget, transferTimestamp);

            // create transfer transaction
            TransferTransactionEntity transferEntity = createTransferTransaction(fromAccountNumber, toAccountNumber, amount, transferTimestamp);
            return map(transferEntity);
        } finally {
            toLock.unlock();
            fromLock.unlock();
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<TransferTransaction> listTransfers(AccountNumber accountNumber) {
        getAccountEntity(accountNumber); // get account entity to check if it exists, otherwise an exception will be thrown
        List<TransferTransactionEntity> entities = persistenceService.listTransfers(accountNumber.getNumber());

        // map persistence layer entities to business layer objects
        List<TransferTransaction> result = new ArrayList<>(entities.size());
        for (TransferTransactionEntity entity : entities) {
            TransferTransaction transaction = map(entity);
            result.add(transaction);
        }

        // sort the result
        result.sort(Comparator.comparing(TransferTransaction::getTimestamp).reversed());
        return result;
    }

    /**
     * Maps a persistence layer account entity to a business-layer {@link BankAccount} object.
     * <p>
     * This conversion translates the database representation of an account into a form
     * suitable for business layer.
     *
     * @param entity entity to map
     * @return bank account
     */
    private BankAccount map(AccountEntity entity) {
        AccountNumber accountNumber = AccountNumber.of(entity.getNumber());
        BigDecimal balance = new BigDecimal(entity.getBalance());
        return new BankAccount(accountNumber, balance);
    }

    /**
     * Maps a persistence layer deposit entity {@link DepositTransactionEntity} to a business layer {@link DepositTransaction} object.
     *
     * @param entity entity to map
     * @return deposit transaction
     */
    private DepositTransaction map(DepositTransactionEntity entity) {
        TransactionId transactionId = new TransactionId(entity.getId());
        AccountNumber accountNumber = AccountNumber.of(entity.getAccountNumber());
        BigDecimal amount = new BigDecimal(entity.getAmount());
        Instant timestamp = Instant.ofEpochMilli(entity.getTimestamp());
        return new DepositTransaction(transactionId, accountNumber, amount, timestamp);
    }

    /**
     * Maps a persistence layer withdrawal entity {@link WithdrawTransactionEntity} to a business layer {@link WithdrawTransaction} object.
     *
     * @param entity entity to map
     * @return withdrawal transaction
     */
    private WithdrawTransaction map(WithdrawTransactionEntity entity) {
        TransactionId transactionId = new TransactionId(entity.getId());
        AccountNumber accountNumber = AccountNumber.of(entity.getAccountNumber());
        BigDecimal amount = new BigDecimal(entity.getAmount());
        Instant timestamp = Instant.ofEpochMilli(entity.getTimestamp());
        return new WithdrawTransaction(transactionId, accountNumber, amount, timestamp);
    }

    /**
     * Maps a persistence layer transfer entity {@link TransferTransactionEntity} to a business layer {@link TransferTransaction} object
     *
     * @param entity entity to map
     * @return transfer transaction
     */
    private TransferTransaction map(TransferTransactionEntity entity) {
        TransactionId transactionId = new TransactionId(entity.getId());
        AccountNumber fromAccountNumber = AccountNumber.of(entity.getFromAccountNumber());
        AccountNumber toAccountNumber = AccountNumber.of(entity.getToAccountNumber());
        BigDecimal amount = new BigDecimal(entity.getAmount());
        Instant timestamp = Instant.ofEpochMilli(entity.getTimestamp());
        return new TransferTransaction(transactionId, fromAccountNumber, toAccountNumber, amount, timestamp);
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

    /**
     * Retrieves an account entity by the provided accountNumber or creates a new one.
     * <p>
     * If the account already exists, it will be returned.
     * Otherwise, a new account entity will be created and returned.
     *
     * @param accountNumber account number
     * @return existing or freshly created account with provided number
     */
    AccountEntity getOrCreateAccountEntity(AccountNumber accountNumber) {
        Optional<AccountEntity> optionalAccountEntity = persistenceService.getAccount(accountNumber.getNumber());
        if (optionalAccountEntity.isPresent()) {
            return optionalAccountEntity.get();
        } else {

            // If not found, create a new account
            long accountCreationTimestamp = clock.instant().toEpochMilli();
            return persistenceService.createAccount(accountNumber.getNumber(), INITIAL_BALANCE, accountCreationTimestamp);
        }
    }

    /**
     * Retrieves an account entity by the provided accountNumber.
     * <p>
     * If the account cannot be found, an exception will be thrown.
     *
     * @param accountNumber account number
     * @return account entity
     * @throws AccountNotFoundException if account entity by the given account number doesn't exist
     */
    AccountEntity getAccountEntity(AccountNumber accountNumber) {
        Optional<AccountEntity> optionalAccountEntity = persistenceService.getAccount(accountNumber.getNumber());
        if (optionalAccountEntity.isPresent()) {
            return optionalAccountEntity.get();
        } else {
            throw new AccountNotFoundException("account by number " + accountNumber + " doesn't exist", accountNumber);
        }
    }

    /**
     * Create and save a deposit transaction for a given account with a specific amount.
     * <p>
     * Method doesn't change the actual balance of the given account
     *
     * @param accountNumber        account number
     * @param amount               amount of deposit
     * @param transactionTimestamp timestamp of deposit
     */
    void createDepositTransaction(AccountNumber accountNumber, BigDecimal amount, long transactionTimestamp) {
        TransactionId transactionId = TransactionId.generateNew();
        persistenceService.createDepositTransaction(transactionId.getId(), accountNumber.getNumber(), amount.toPlainString(), transactionTimestamp);
    }

    /**
     * Create and save a withdrawal transaction for a given account with a specific amount.
     * <p>
     * Method doesn't change the actual balance of the given account
     *
     * @param accountNumber        account number
     * @param amount               amount of withdrawal
     * @param transactionTimestamp timestamp of withdrawal
     */
    void createWithdrawTransaction(AccountNumber accountNumber, BigDecimal amount, long transactionTimestamp) {
        TransactionId transactionId = TransactionId.generateNew();
        persistenceService.createWithdrawTransaction(transactionId.getId(), accountNumber.getNumber(), amount.toPlainString(), transactionTimestamp);
    }

    /**
     * Create and save a transfer transaction with a specific amount
     *
     * @param fromAccountNumber    transfer initiator account
     * @param toAccountNumber      transfer target amount
     * @param amount               amount of transfer
     * @param transactionTimestamp timestamp of transfer
     */
    TransferTransactionEntity createTransferTransaction(AccountNumber fromAccountNumber, AccountNumber toAccountNumber, BigDecimal amount, long transactionTimestamp) {
        TransactionId transactionId = TransactionId.generateNew();
        return persistenceService.createTransferTransaction(transactionId.getId(), fromAccountNumber.getNumber(), toAccountNumber.getNumber(), amount.toPlainString(), transactionTimestamp);
    }

    /**
     * Update the balance for a specified account entity
     *
     * @param accountEntity        existing account entity retrieved from persistence layer
     * @param newBalance           new balance to be set for the given account
     * @param transactionTimestamp timestamp of balance update
     * @return account object with an updated balance
     */
    BankAccount updateBalance(AccountEntity accountEntity, BigDecimal newBalance, long transactionTimestamp) {
        AccountEntity updatedAccountEntity = persistenceService.updateAccountBalance(accountEntity, newBalance.toPlainString(), transactionTimestamp);
        return map(updatedAccountEntity);
    }

    @Autowired
    public void setPersistenceService(BankPersistenceService persistenceService) {
        this.persistenceService = persistenceService;
    }
}

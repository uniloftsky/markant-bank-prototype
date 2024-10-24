package net.uniloftsky.markant.bank.biz.persistence;

import java.util.List;
import java.util.UUID;

/**
 * Bank persistence service.
 * Responsible only for saving and retrieving data from the database.
 */
public interface BankPersistenceService {

    /**
     * Create account with provided parameters
     *
     * @param accountNumber     account number
     * @param balance           account balance
     * @param creationTimestamp creation timestamp. Should be provided in milliseconds
     * @return created account entity
     * @throws IllegalArgumentException if parameters are invalid
     */
    AccountEntity createAccount(long accountNumber, String balance, long creationTimestamp);

    /**
     * Get account by account number
     *
     * @param accountNumber account number
     * @return account entity
     * @throws AccountNotFoundPersistenceServiceException in case if account by the provided ID cannot be found
     */
    AccountEntity getAccount(long accountNumber);

    /**
     * Update balance for an existing account.
     * <p>
     * Method has a side effect: the provided accountEntity will be modified with a new balance and timestamp
     *
     * @param accountEntity existing account entity
     * @param newBalance    new balance value
     * @param timestamp     timestamp of the balance update. Should be specified in milliseconds
     * @return updated account entity
     */
    AccountEntity updateAccountBalance(AccountEntity accountEntity, String newBalance, long timestamp);

    /**
     * Retrieves a list of deposit transactions for the specified account.
     * <p>
     * The returned list is not sorted and reflects the order in which the transactions were saved in the database
     *
     * @param accountNumber account number
     * @return list of deposit transactions
     */
    List<DepositTransactionEntity> listDeposits(long accountNumber);

    /**
     * Create and save a deposit transaction for a given account number and with specified amount
     *
     * @param id            deposit transaction ID
     * @param accountNumber deposit owner, account number
     * @param amount        amount of deposit
     * @param timestamp     timestamp of the deposit transaction
     * @return created deposit transaction entity
     */
    DepositTransactionEntity createDepositTransaction(UUID id, long accountNumber, String amount, long timestamp);

    /**
     * Retrieves a list of withdrawal transactions for the specified account.
     * <p>
     * The returned list is not sorted and reflects the order in which the transactions were saved in the database
     *
     * @param accountNumber account number
     * @return list of withdrawal transactions
     */
    List<WithdrawTransactionEntity> listWithdrawals(long accountNumber);

    /**
     * Create and save a withdrawal transaction for a given account number and with specified amount
     *
     * @param id            withdrawal transaction ID
     * @param accountNumber withdrawal owner, account number
     * @param amount        amount of withdrawal
     * @param timestamp     timestamp of the withdrawal transaction
     * @return created withdrawal transaction entity
     */
    WithdrawTransactionEntity createWithdrawTransaction(UUID id, long accountNumber, String amount, long timestamp);

}

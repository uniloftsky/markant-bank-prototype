package net.uniloftsky.markant.bank.biz.persistence;

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
     * Method has a side effect: the provided accountEntity will be modified with a new balance and timestamp
     *
     * @param accountEntity existing account entity
     * @param newBalance    new balance value
     * @param timestamp     timestamp of the balance update. Should be specified in milliseconds
     * @return updated account entity
     */
    AccountEntity updateAccountBalance(AccountEntity accountEntity, String newBalance, long timestamp);

    DepositTransactionEntity createDepositTransaction(UUID id, long accountNumber, String amount, long timestamp);

    WithdrawTransactionEntity createWithdrawTransaction(UUID id, long accountNumber, String amount, long timestamp);

}

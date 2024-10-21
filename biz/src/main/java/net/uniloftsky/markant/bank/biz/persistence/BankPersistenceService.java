package net.uniloftsky.markant.bank.biz.persistence;

/**
 * Bank persistence service.
 * Responsible only for saving and retrieving data from the database.
 */
public interface BankPersistenceService {

    /**
     * Create account with provided parameters
     *
     * @param accountNumber     account number
     * @param creationTimestamp creation timestamp. Should be provided in milliseconds
     * @return created account entity
     * @throws IllegalArgumentException if parameters are invalid
     */
    AccountEntity createAccount(long accountNumber, long creationTimestamp);

    /**
     * Get account by account number
     *
     * @param accountNumber account number
     * @return account entity
     * @throws AccountNotFoundPersistenceServiceException in case if account by the provided ID cannot be found
     */
    AccountEntity getAccount(long accountNumber);

    /**
     * Update balance for an EXISTING account.
     *
     * @param accountEntity EXISTING account entity
     * @param newBalance    new balance value
     * @param timestamp     timestamp of the balance update. Should be specified in milliseconds
     * @return updated account entity
     */
    AccountEntity updateAccountBalance(AccountEntity accountEntity, String newBalance, long timestamp);

}

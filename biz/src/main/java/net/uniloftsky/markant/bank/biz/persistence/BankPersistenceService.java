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
    AccountEntity createAccount(int accountNumber, long creationTimestamp);

    /**
     * Get account by account number
     *
     * @param accountNumber account number
     * @return account entity
     * @throws AccountNotFoundPersistenceServiceException in case if account by the provided ID cannot be found
     */
    AccountEntity getAccount(int accountNumber) throws AccountNotFoundPersistenceServiceException;

    /**
     * Update account balance
     *
     * @param accountNumber account number
     * @param newBalance    new account balance
     * @param timestamp     timestamp of the balance update. Should be specified in milliseconds
     * @return updated account entity
     */
    AccountEntity updateAccountBalance(int accountNumber, double newBalance, long timestamp);

}

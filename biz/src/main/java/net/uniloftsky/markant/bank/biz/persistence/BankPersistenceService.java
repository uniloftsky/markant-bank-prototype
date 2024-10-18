package net.uniloftsky.markant.bank.biz.persistence;

import net.uniloftsky.markant.bank.biz.BankAccount;

/**
 * Bank persistence service
 */
public interface BankPersistenceService {

    /**
     * Create account with provided parameters
     *
     * @param accountId         account id
     * @param creationTimestamp creation timestamp. Should be provided in milliseconds
     * @return created account entity
     * @throws IllegalArgumentException if parameters are invalid
     */
    AccountEntity createAccount(BankAccount.AccountId accountId, long creationTimestamp);

    /**
     * Get account by account ID
     *
     * @param accountId account id
     * @return account entity
     * @throws AccountNotFoundPersistenceServiceException in case if account by the provided ID cannot be found
     */
    AccountEntity getAccount(BankAccount.AccountId accountId) throws BankPersistenceServiceException;

}

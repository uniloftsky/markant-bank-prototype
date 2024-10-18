package net.uniloftsky.markant.bank.biz.persistence;

/**
 * Exception that is thrown in case when account with specific ID cannot be found
 */
public class AccountNotFoundPersistenceServiceException extends BankPersistenceServiceException {

    public AccountNotFoundPersistenceServiceException() {
    }

    public AccountNotFoundPersistenceServiceException(String message) {
        super(message);
    }

    public AccountNotFoundPersistenceServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}

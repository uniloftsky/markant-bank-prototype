package net.uniloftsky.markant.bank.biz.persistence;

/**
 * Exception thrown when account with specific ID cannot be found
 */
public class AccountNotFoundPersistenceServiceException extends BankPersistenceServiceException {

    /**
     * Account number
     */
    private final long accountNumber;

    public AccountNotFoundPersistenceServiceException(long accountNumber) {
        this.accountNumber = accountNumber;
    }

    public AccountNotFoundPersistenceServiceException(String message, long accountNumber) {
        super(message);
        this.accountNumber = accountNumber;
    }

    public AccountNotFoundPersistenceServiceException(String message, Throwable cause, long accountNumber) {
        super(message, cause);
        this.accountNumber = accountNumber;
    }

    public long getAccountNumber() {
        return accountNumber;
    }
}

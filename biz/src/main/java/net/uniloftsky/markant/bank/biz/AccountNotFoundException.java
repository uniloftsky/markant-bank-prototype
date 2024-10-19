package net.uniloftsky.markant.bank.biz;

/**
 * Exception thrown when account with specific ID cannot be found
 */
public class AccountNotFoundException extends BankServiceException {

    public AccountNotFoundException() {
    }

    public AccountNotFoundException(String message) {
        super(message);
    }

    public AccountNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}

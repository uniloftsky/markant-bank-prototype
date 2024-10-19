package net.uniloftsky.markant.bank.biz;

/**
 * Exception thrown when invalid account number is specified
 */
public class InvalidAccountNumberException extends BankServiceException {

    public InvalidAccountNumberException() {
    }

    public InvalidAccountNumberException(String message) {
        super(message);
    }

    public InvalidAccountNumberException(String message, Throwable cause) {
        super(message, cause);
    }
}

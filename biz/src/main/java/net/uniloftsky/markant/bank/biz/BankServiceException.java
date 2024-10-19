package net.uniloftsky.markant.bank.biz;

/**
 * Root exception for {@link BankService}
 */
public class BankServiceException extends Exception {

    public BankServiceException() {
    }

    public BankServiceException(String message) {
        super(message);
    }

    public BankServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}

package net.uniloftsky.markant.bank.biz;

/**
 * Exception thrown when the transaction amount exceeds the current account balance
 */
public class InsufficientBalanceException extends BankServiceException {

    public InsufficientBalanceException() {
    }

    public InsufficientBalanceException(String message) {
        super(message);
    }

    public InsufficientBalanceException(String message, Throwable cause) {
        super(message, cause);
    }
}

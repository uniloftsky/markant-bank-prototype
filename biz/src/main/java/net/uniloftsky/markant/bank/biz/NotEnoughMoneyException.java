package net.uniloftsky.markant.bank.biz;

/**
 * Exception thrown when the transaction amount exceeds the current account balance
 */
public class NotEnoughMoneyException extends BankServiceException {

    public NotEnoughMoneyException() {
    }

    public NotEnoughMoneyException(String message) {
        super(message);
    }

    public NotEnoughMoneyException(String message, Throwable cause) {
        super(message, cause);
    }
}

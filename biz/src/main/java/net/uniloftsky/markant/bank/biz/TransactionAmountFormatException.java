package net.uniloftsky.markant.bank.biz;

/**
 * Exception thrown if transaction amount format is invalid
 */
public class TransactionAmountFormatException extends BankServiceException {

    /**
     * Invalid transaction amount
     */
    private final String amount;

    public TransactionAmountFormatException(String amount) {
        this.amount = amount;
    }

    public TransactionAmountFormatException(String message, String amount) {
        super(message);
        this.amount = amount;
    }

    public TransactionAmountFormatException(String message, Throwable cause, String amount) {
        super(message, cause);
        this.amount = amount;
    }

    public String getAmount() {
        return amount;
    }
}

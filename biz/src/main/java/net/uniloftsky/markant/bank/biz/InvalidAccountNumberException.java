package net.uniloftsky.markant.bank.biz;

/**
 * Exception thrown when invalid account number is specified
 */
public class InvalidAccountNumberException extends BankServiceException {

    /**
     * Invalid account number
     */
    private final String accountNumber;

    public InvalidAccountNumberException(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public InvalidAccountNumberException(String message, String accountNumber) {
        super(message);
        this.accountNumber = accountNumber;
    }

    public InvalidAccountNumberException(String message, Throwable cause, String accountNumber) {
        super(message, cause);
        this.accountNumber = accountNumber;
    }

    public String getAccountNumber() {
        return accountNumber;
    }
}

package net.uniloftsky.markant.bank.biz;

/**
 * Exception thrown when account with specific number cannot be found
 */
public class AccountNotFoundException extends BankServiceException {

    private final AccountNumber accountNumber;

    public AccountNotFoundException(AccountNumber accountNumber) {
        this.accountNumber = accountNumber;
    }

    public AccountNotFoundException(String message, AccountNumber accountNumber) {
        super(message);
        this.accountNumber = accountNumber;
    }

    public AccountNotFoundException(String message, Throwable cause, AccountNumber accountNumber) {
        super(message, cause);
        this.accountNumber = accountNumber;
    }

    public AccountNumber getAccountNumber() {
        return accountNumber;
    }
}

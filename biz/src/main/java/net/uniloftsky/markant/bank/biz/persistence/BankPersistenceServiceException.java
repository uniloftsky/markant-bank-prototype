package net.uniloftsky.markant.bank.biz.persistence;

public class BankPersistenceServiceException extends RuntimeException {

    public BankPersistenceServiceException() {
    }

    public BankPersistenceServiceException(String message) {
        super(message);
    }

    public BankPersistenceServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}

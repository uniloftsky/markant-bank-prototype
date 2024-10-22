package net.uniloftsky.markant.bank.rest;

/**
 * Error keys for exceptions
 */
public enum ErrorKey {

    /**
     * Key when client tries to use non-existing API
     */
    API_NOT_FOUND_ERROR("api.not.found.error"),

    /**
     * Key for internal system error
     */
    SYSTEM_INTERNAL_ERROR("system.internal.error"),

    /**
     * Error when requested bank account cannot be found
     */
    ACCOUNT_NOT_FOUND_ERROR("account.not.found.error"),

    /**
     * Error if the transaction amount exceeds the current account balance
     */
    INSUFFICIENT_BALANCE_ERROR("insufficient.balance.error"),

    /**
     * Error if provided account number has invalid format
     */
    INVALID_ACCOUNT_NUMBER_ERROR("invalid.account.number.error"),

    /**
     * Error if provided transaction amount format is invalid. For example 100,50 instead of 100.50
     */
    TRANSACTION_AMOUNT_FORMAT_ERROR("transaction.amount.format.error");

    /**
     * Message key corresponding to "exceptions-description.properties"
     */
    private final String messageKey;

    ErrorKey(String messageKey) {
        this.messageKey = messageKey;
    }

    public String getMessageKey() {
        return messageKey;
    }
}

package net.uniloftsky.markant.bank.rest;

/**
 * Error keys for exceptions
 */
public enum ErrorKey {

    /**
     * Key when client tries to use non-existing API
     */
    API_NOT_FOUND_ERROR,

    /**
     * Key for internal system error
     */
    SYSTEM_INTERNAL_ERROR,

    /**
     * Error when requested bank account cannot be found
     */
    ACCOUNT_NOT_FOUND,

    /**
     * Error if the transaction amount exceeds the current account balance
     */
    INSUFFICIENT_BALANCE_ERROR,

    /**
     * Error if provided account number has invalid format
     */
    INVALID_ACCOUNT_NUMBER_ERROR;

}

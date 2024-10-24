package net.uniloftsky.markant.bank.biz;

/**
 * Type of the bank finance operation
 */
public enum BankTransactionType {

    /**
     * Deposit operation
     */
    DEPOSIT,

    /**
     * Withdrawal operation
     */
    WITHDRAWAL,

    /**
     * Transfer transaction to another account
     */
    TRANSFER

}

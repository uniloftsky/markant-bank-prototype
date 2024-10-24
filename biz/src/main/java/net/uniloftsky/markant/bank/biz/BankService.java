package net.uniloftsky.markant.bank.biz;

import java.math.BigDecimal;
import java.util.List;

/**
 * Bank service to manage banking accounts and transactions
 */
public interface BankService {

    /**
     * Get account by provided account number
     *
     * @param accountNumber account number
     * @return bank account
     * @throws AccountNotFoundException if account by provided number cannot be found
     */
    BankAccount getAccount(AccountNumber accountNumber);

    /**
     * Withdraw the provided amount of money from account
     *
     * @param accountNumber account ID
     * @param amount        money amount
     * @throws AccountNotFoundException     if account by provided number cannot be found
     * @throws InsufficientBalanceException if withdrawal amount is greater than account balance
     */
    BankAccount withdraw(AccountNumber accountNumber, BigDecimal amount);

    /**
     * Get list of withdrawal transactions for the specified account.
     * Transactions are sorted by timestamp in descending order (most recent first)
     *
     * @param accountNumber account number
     * @return list of withdrawal transactions
     */
    List<WithdrawTransaction> listWithdrawals(AccountNumber accountNumber);

    /**
     * Deposit the provided amount of money to account
     *
     * @param accountNumber account ID
     * @param amount        money amount
     */
    BankAccount deposit(AccountNumber accountNumber, BigDecimal amount);

    /**
     * Get list of deposit transactions for the specified account.
     * Transactions are sorted by timestamp in descending order (most recent first)
     *
     * @param accountNumber account number
     * @return list of deposit transactions
     */
    List<DepositTransaction> listDeposits(AccountNumber accountNumber);

    /**
     * Get list of all transactions (deposits, withdrawals, transfers) for the specified account.
     * Transactions are sorted by timestamp in descending order (most recent first)
     *
     * @param accountNumber account number
     * @return list of all transactions
     */
    List<BankTransaction> listTransactions(AccountNumber accountNumber);

}

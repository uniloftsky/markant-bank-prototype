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
     * @throws AccountNotFoundException if account by provided number cannot be found
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
     * @throws AccountNotFoundException if account by provided number cannot be found
     */
    List<DepositTransaction> listDeposits(AccountNumber accountNumber);

    /**
     * Get list of all transactions (deposits, withdrawals, transfers) for the specified account.
     * Transactions are sorted by timestamp in descending order (most recent first)
     *
     * @param accountNumber account number
     * @return list of all transactions
     * @throws AccountNotFoundException if account by provided number cannot be found
     */
    List<BankTransaction> listTransactions(AccountNumber accountNumber);

    /**
     * Transfer money from one account to another
     *
     * @param fromAccountNumber transfer initiator account number
     * @param toAccountNumber   transfer target account number
     * @param amount            transfer amount of money
     * @return transfer transaction
     */
    TransferTransaction transfer(AccountNumber fromAccountNumber, AccountNumber toAccountNumber, BigDecimal amount);

    /**
     * Get list of transfer transactions for the specified account.
     * Transactions are sorted by timestamp in descending order (most recent first).
     *
     * @param accountNumber account number
     * @return list of transfer
     * @throws AccountNotFoundException if account by provided number cannot be found
     */
    List<TransferTransaction> listTransfers(AccountNumber accountNumber);

}

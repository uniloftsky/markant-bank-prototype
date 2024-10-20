package net.uniloftsky.markant.bank.biz;

import java.math.BigDecimal;

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
    BankAccount getAccount(AccountNumber accountNumber) throws AccountNotFoundException;

    /**
     * Withdraw the provided amount of money from account
     *
     * @param accountNumber account ID
     * @param amount        money amount
     */
    BankAccount withdraw(AccountNumber accountNumber, BigDecimal amount) throws AccountNotFoundException, NotEnoughMoneyException;

    /**
     * Deposit the provided amount of money to account
     *
     * @param accountNumber account ID
     * @param amount        money amount
     */
    BankAccount deposit(AccountNumber accountNumber, BigDecimal amount);

}

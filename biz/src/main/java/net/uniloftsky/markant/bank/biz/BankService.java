package net.uniloftsky.markant.bank.biz;

import java.math.BigDecimal;

/**
 * Bank service to manage banking accounts and transactions
 */
public interface BankService {

    BankAccount getAccount(BankAccount.AccountNumber accountNumber) throws AccountNotFoundException;

    /**
     * Withdraw the provided amount of money from account
     *
     * @param accountNumber account ID
     * @param amount    money amount
     */
    BankAccount withdraw(BankAccount.AccountNumber accountNumber, BigDecimal amount) throws AccountNotFoundException, NotEnoughMoneyException;

    /**
     * Deposit the provided amount of money to account
     *
     * @param accountNumber account ID
     * @param amount    money amount
     */
    BankAccount deposit(BankAccount.AccountNumber accountNumber, BigDecimal amount);

}

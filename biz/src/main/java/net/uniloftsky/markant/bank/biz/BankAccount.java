package net.uniloftsky.markant.bank.biz;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * Class that represents a bank account
 */
public final class BankAccount {

    /**
     * Account id
     */
    private final AccountNumber number;

    /**
     * Account balance
     */
    private final BigDecimal balance;

    /**
     * Public constructor with arguments
     *
     * @param number  account id
     * @param balance account balance
     */
    public BankAccount(AccountNumber number, BigDecimal balance) {
        this.number = number;
        this.balance = balance;
    }

    public AccountNumber getNumber() {
        return number;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BankAccount that = (BankAccount) o;
        return Objects.equals(number, that.number);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(number);
    }

    @Override
    public String toString() {
        return "BankAccount{" +
                "id=" + number +
                ", balance=" + balance +
                '}';
    }

}

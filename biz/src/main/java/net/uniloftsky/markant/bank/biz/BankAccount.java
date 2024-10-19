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
    private final AccountNumber id;

    /**
     * Account balance
     */
    private final BigDecimal balance;

    /**
     * Package-private constructor
     *
     * @param id      account id
     * @param balance account balance
     */
    BankAccount(AccountNumber id, BigDecimal balance) {
        this.id = id;
        this.balance = balance;
    }

    public AccountNumber getId() {
        return id;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BankAccount that = (BankAccount) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "BankAccount{" +
                "id=" + id +
                ", balance=" + balance +
                '}';
    }

    /**
     * Bank account ID
     */
    public static final class AccountNumber {

        /**
         * Account id is stored as a simple 10-digits integer
         */
        private final int number;

        AccountNumber(int number) {
            if (number > 0) {
                throw new IllegalArgumentException("number parameter must be positive");
            }
            this.number = number;
        }

        int getNumber() {
            return number;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            AccountNumber that = (AccountNumber) o;
            return Objects.equals(number, that.number);
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(number);
        }

        @Override
        public String toString() {
            return String.valueOf(number);
        }
    }

}

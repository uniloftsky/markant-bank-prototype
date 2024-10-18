package net.uniloftsky.markant.bank.biz;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

/**
 * Class that represents a bank account
 */
public final class BankAccount {

    /**
     * Account id
     */
    private final AccountId id;

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
    BankAccount(AccountId id, BigDecimal balance) {
        this.id = id;
        this.balance = balance;
    }

    public AccountId getId() {
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
    public static final class AccountId {

        /**
         * Account id is stored as {@link UUID}
         */
        private final UUID id;

        AccountId(UUID id) {
            this.id = id;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            AccountId that = (AccountId) o;
            return Objects.equals(id, that.id);
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(id);
        }

        @Override
        public String toString() {
            return id.toString();
        }
    }

}

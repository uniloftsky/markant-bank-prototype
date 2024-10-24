package net.uniloftsky.markant.bank.biz;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;

/**
 * Withdrawal transaction within a single account
 */
public final class WithdrawTransaction implements BankTransaction {

    /**
     * Type of transaction
     */
    private static final TransactionType TYPE = TransactionType.WITHDRAWAL;

    /**
     * Transaction ID
     */
    private final TransactionId id;

    /**
     * Transaction owner, Account number
     */
    private final AccountNumber accountNumber;

    /**
     * Amount of withdrawal
     */
    private final BigDecimal amount;

    /**
     * Timestamp of withdrawal
     */
    private final Instant timestamp;

    /**
     * Public constructor with arguments
     *
     * @param id            transaction ID
     * @param accountNumber transaction owner, account number
     * @param amount        amount of withdrawal
     * @param timestamp     timestamp of withdrawal
     */
    public WithdrawTransaction(TransactionId id, AccountNumber accountNumber, BigDecimal amount, Instant timestamp) {
        this.id = id;
        this.accountNumber = accountNumber;
        this.amount = amount;
        this.timestamp = timestamp;
    }

    @Override
    public TransactionId getId() {
        return id;
    }

    public AccountNumber getAccountNumber() {
        return accountNumber;
    }

    @Override
    public BigDecimal getAmount() {
        return amount;
    }

    @Override
    public Instant getTimestamp() {
        return timestamp;
    }

    @Override
    public TransactionType getType() {
        return TYPE;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WithdrawTransaction that = (WithdrawTransaction) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "{" +
                "id=" + id +
                ", accountNumber=" + accountNumber +
                ", amount=" + amount +
                ", timestamp=" + timestamp +
                '}';
    }
}

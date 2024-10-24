package net.uniloftsky.markant.bank.biz;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;

public final class WithdrawTransaction implements BankTransaction {

    private static final BankTransactionType TYPE = BankTransactionType.WITHDRAWAL;

    private final TransactionId id;

    private final AccountNumber accountNumber;

    private final BigDecimal amount;

    private final Instant timestamp;

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
    public BankTransactionType getType() {
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

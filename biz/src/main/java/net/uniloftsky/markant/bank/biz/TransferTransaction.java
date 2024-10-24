package net.uniloftsky.markant.bank.biz;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;

/**
 * Bank finance transfer transaction from one account to another
 */
public final class TransferTransaction implements BankTransaction {

    private static final BankTransactionType TYPE = BankTransactionType.TRANSFER;

    private final TransactionId id;

    /**
     * Transfer initiator account number
     */
    private final AccountNumber from;

    /**
     * Transfer target account number
     */
    private final AccountNumber to;

    private final BigDecimal amount;

    private final Instant timestamp;

    public TransferTransaction(TransactionId id, BigDecimal amount, Instant timestamp, AccountNumber to, AccountNumber from) {
        this.id = id;
        this.amount = amount;
        this.timestamp = timestamp;
        this.to = to;
        this.from = from;
    }

    @Override
    public TransactionId getId() {
        return id;
    }

    public AccountNumber getFrom() {
        return from;
    }

    public AccountNumber getTo() {
        return to;
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
        TransferTransaction that = (TransferTransaction) o;
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
                ", from=" + from +
                ", to=" + to +
                ", amount=" + amount +
                ", timestamp=" + timestamp +
                '}';
    }
}

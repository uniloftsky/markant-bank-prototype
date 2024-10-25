package net.uniloftsky.markant.bank.biz;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;

/**
 * Bank finance transfer transaction from one account to another
 */
public final class TransferTransaction implements BankTransaction {

    /**
     * Transaction type
     */
    private static final TransactionType TYPE = TransactionType.TRANSFER;

    /**
     * Transfer id
     */
    private final TransactionId id;

    /**
     * Transfer initiator account number
     */
    private final AccountNumber fromAccountNumber;

    /**
     * Transfer target account number
     */
    private final AccountNumber toAccountNumber;

    /**
     * Amount of transfer
     */
    private final BigDecimal amount;

    /**
     * Timestamp of transfer
     */
    private final Instant timestamp;

    /**
     * Public constructor with arguments
     *
     * @param id        transfer ID
     * @param amount    transfer amount
     * @param timestamp transfer timestamp
     * @param toAccountNumber        transfer initiator account number
     * @param fromAccountNumber      transfer target account number
     */
    public TransferTransaction(TransactionId id, AccountNumber fromAccountNumber, AccountNumber toAccountNumber, BigDecimal amount, Instant timestamp) {
        this.id = id;
        this.amount = amount;
        this.timestamp = timestamp;
        this.fromAccountNumber = fromAccountNumber;
        this.toAccountNumber = toAccountNumber;
    }

    @Override
    public TransactionId getId() {
        return id;
    }

    public AccountNumber getFromAccountNumber() {
        return fromAccountNumber;
    }

    public AccountNumber getToAccountNumber() {
        return toAccountNumber;
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
                ", from=" + fromAccountNumber +
                ", to=" + toAccountNumber +
                ", amount=" + amount +
                ", timestamp=" + timestamp +
                '}';
    }
}

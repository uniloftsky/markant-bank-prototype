package net.uniloftsky.markant.bank.biz.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "deposit_transaction")
public class DepositTransactionEntity {

    @Id
    @Column(name = "id")
    private UUID id;

    @Column(name = "amount")
    private String amount;

    @Column(name = "timestamp")
    private long timestamp;

    @Column(name = "account_number")
    private long accountNumber;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public long getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(long accountNumber) {
        this.accountNumber = accountNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DepositTransactionEntity that = (DepositTransactionEntity) o;
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
                ", amount='" + amount + '\'' +
                ", timestamp=" + timestamp +
                ", accountNumber=" + accountNumber +
                '}';
    }
}

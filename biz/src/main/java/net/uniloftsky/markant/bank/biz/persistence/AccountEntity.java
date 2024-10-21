package net.uniloftsky.markant.bank.biz.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.Objects;

/**
 * Persistence account entity
 */
@Entity
@Table(name = "bank_account")
public class AccountEntity {

    @Id
    @Column(name = "number", nullable = false, updatable = false)
    private long number;

    @Column(name = "balance")
    private String balance;

    @Column(name = "created_at", nullable = false)
    private long createdAt;

    @Column(name = "updated_at")
    private long updatedAt;

    public long getNumber() {
        return number;
    }

    public void setNumber(long id) {
        this.number = id;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public long getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(long updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AccountEntity that = (AccountEntity) o;
        return Objects.equals(number, that.number);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(number);
    }

    @Override
    public String toString() {
        return "AccountEntity{" +
                "number='" + number + '\'' +
                ", balance=" + balance +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}

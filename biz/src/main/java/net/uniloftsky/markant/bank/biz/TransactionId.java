package net.uniloftsky.markant.bank.biz;

import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.Objects;
import java.util.UUID;

/**
 * Bank transaction ID
 */
@JsonSerialize(using = BankSerializers.TransactionIdSerializer.class)
public final class TransactionId {

    @JsonValue
    private final UUID id;

    TransactionId(UUID id) {
        this.id = id;
    }

    public static TransactionId generateNew() {
        return new TransactionId(UUID.randomUUID());
    }

    UUID getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TransactionId that = (TransactionId) o;
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

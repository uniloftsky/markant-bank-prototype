package net.uniloftsky.markant.bank.biz;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

/**
 * Class that represents a bank finance operation
 */
public final class BankOperation {

    private final OperationId id;
    private final BankAccount.AccountId accountId;
    private final BigDecimal amount;
    private final BankOperationType type;

    /**
     * Package-private constructor
     *
     * @param id        operation id
     * @param accountId account id
     * @param amount    amount of transferred money
     * @param type      operation type
     */
    BankOperation(OperationId id, BankAccount.AccountId accountId, BigDecimal amount, BankOperationType type) {
        this.id = id;
        this.accountId = accountId;
        this.amount = amount;
        this.type = type;
    }

    public OperationId getId() {
        return id;
    }

    public BankAccount.AccountId getAccountId() {
        return accountId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public BankOperationType getType() {
        return type;
    }

    /**
     * Finance bank operation ID
     */
    public static final class OperationId {

        private final UUID id;

        OperationId(UUID id) {
            this.id = id;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            OperationId that = (OperationId) o;
            return Objects.equals(id, that.id);
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(id);
        }

        @Override
        public String toString() {
            return "BankOperationId{" +
                    "id=" + id +
                    '}';
        }
    }

}

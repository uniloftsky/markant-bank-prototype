package net.uniloftsky.markant.bank.biz;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * Generic interface for bank transactions.
 * Defines the common behaviour for all transactions
 */
public interface BankTransaction {

    /**
     * Transaction ID
     *
     * @return ID of the transaction
     */
    TransactionId getId();

    /**
     * Transaction amount
     *
     * @return amount of the transaction
     */
    BigDecimal getAmount();

    /**
     * Transaction type
     *
     * @return type of the transaction
     */
    TransactionType getType();

    /**
     * Transaction timestamp
     *
     * @return timestamp when the transaction occurred
     */
    @Schema(type = "integer", format = "int64", description = "Timestamp in milliseconds")
    Instant getTimestamp();

}

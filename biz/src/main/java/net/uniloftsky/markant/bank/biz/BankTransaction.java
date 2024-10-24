package net.uniloftsky.markant.bank.biz;

import java.math.BigDecimal;
import java.time.Instant;

public interface BankTransaction {

    TransactionId getId();

    BigDecimal getAmount();

    BankTransactionType getType();

    Instant getTimestamp();

}

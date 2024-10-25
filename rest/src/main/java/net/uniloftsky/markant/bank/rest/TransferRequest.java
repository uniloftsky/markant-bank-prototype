package net.uniloftsky.markant.bank.rest;

import net.uniloftsky.markant.bank.biz.AccountNumber;
import net.uniloftsky.markant.bank.biz.TransactionAmountFormatException;

import java.math.BigDecimal;

/**
 * Request used to transfer transaction from one account to another
 */
public class TransferRequest {

    /**
     * Transfer target account number
     */
    private long targetAccountNumber;

    /**
     * Transfer amount
     */
    private String amount;

    public AccountNumber getTargetAccountNumber() {
        return AccountNumber.of(targetAccountNumber);
    }

    public void setTargetAccountNumber(long targetAccountNumber) {
        this.targetAccountNumber = targetAccountNumber;
    }

    public BigDecimal getAmount() {
        try {
            return new BigDecimal(amount);
        } catch (NumberFormatException ex) {
            throw new TransactionAmountFormatException(amount);
        }
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "{" +
                "toAccountNumber=" + targetAccountNumber +
                ", amount='" + amount + '\'' +
                '}';
    }
}

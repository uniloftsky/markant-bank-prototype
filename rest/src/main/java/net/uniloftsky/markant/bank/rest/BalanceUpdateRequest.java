package net.uniloftsky.markant.bank.rest;

import net.uniloftsky.markant.bank.biz.TransactionAmountFormatException;

import java.math.BigDecimal;

/**
 * Request used for deposit/withdrawal money from/to specific account number
 */
public final class BalanceUpdateRequest {

    /**
     * Deposit/withdrawal amount
     */
    private String amount;

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
                ", amount='" + amount + '\'' +
                '}';
    }
}

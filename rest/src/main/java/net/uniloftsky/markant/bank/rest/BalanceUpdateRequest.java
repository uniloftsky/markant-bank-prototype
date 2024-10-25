package net.uniloftsky.markant.bank.rest;

import io.swagger.v3.oas.annotations.media.Schema;
import net.uniloftsky.markant.bank.biz.TransactionAmountFormatException;

import java.math.BigDecimal;

/**
 * Request used for deposit/withdrawal money from/to specific account number
 */
@Schema(description = "Request object to deposit/withdraw money from account")
public final class BalanceUpdateRequest {

    /**
     * Deposit/withdrawal amount
     */
    @Schema(description = "The amount of money to deposit/withdraw. The fractional part must be separated by a period (.)", example = "100.25")
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

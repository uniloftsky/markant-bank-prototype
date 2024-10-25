package net.uniloftsky.markant.bank.rest;

import io.swagger.v3.oas.annotations.media.Schema;
import net.uniloftsky.markant.bank.biz.AccountNumber;
import net.uniloftsky.markant.bank.biz.TransactionAmountFormatException;

import java.math.BigDecimal;

/**
 * Request used to transfer transaction from one account to another
 */
@Schema(description = "Request object to transfer money from one account to another")
public class TransferRequest {

    /**
     * Transfer target account number
     */
    @Schema(description = "Target account number to which money will be transferred. Must be a 10 digits long number")
    private long targetAccountNumber;

    /**
     * Transfer amount
     */
    @Schema(description = "The amount of money to transfer. The fractional part must be separated by a period (.)", example = "100.25")
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

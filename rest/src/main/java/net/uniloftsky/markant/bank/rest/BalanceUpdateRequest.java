package net.uniloftsky.markant.bank.rest;

/**
 * Request used for deposit/withdrawal money from/to specific account number
 */
public final class BalanceUpdateRequest {

    /**
     * Deposit/withdrawal amount
     */
    private String amount;

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "BalanceUpdateRequest{" +
                ", amount='" + amount + '\'' +
                '}';
    }
}

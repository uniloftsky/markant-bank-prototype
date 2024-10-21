package net.uniloftsky.markant.bank.rest;

public class TransactionRequest {

    private final long number;
    private final String amount;

    public TransactionRequest(long number, String amount) {
        this.number = number;
        this.amount = amount;
    }

    public long getNumber() {
        return number;
    }

    public String getAmount() {
        return amount;
    }

    @Override
    public String toString() {
        return "TransactionRequest{" +
                "number=" + number +
                ", amount='" + amount + '\'' +
                '}';
    }
}

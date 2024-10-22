package net.uniloftsky.markant.bank.biz;

import java.util.Objects;

/**
 * Bank account ID
 */
public final class AccountNumber {

    // for this prototype 10-digits number should be enough
    private static final long ACCOUNT_NUMBER_MIN = 1_000_000_000L;
    private static final long ACCOUNT_NUMBER_MAX = 9_999_999_999L;

    /**
     * Account id is stored as a simple 10-digits Long
     */
    private final long number;

    private AccountNumber(long number) {
        if (number < ACCOUNT_NUMBER_MIN || number > ACCOUNT_NUMBER_MAX) {
            throw new InvalidAccountNumberException("invalid account number. Account number must be a 10-digit number");
        }
        this.number = number;
    }

    public static AccountNumber of(long number) {
        return new AccountNumber(number);
    }

    long getNumber() {
        return number;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AccountNumber that = (AccountNumber) o;
        return Objects.equals(number, that.number);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(number);
    }

    @Override
    public String toString() {
        return String.valueOf(number);
    }
}

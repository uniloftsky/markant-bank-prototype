package net.uniloftsky.markant.bank.biz;

import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Objects;

/**
 * Bank account ID
 */
public final class AccountNumber {

    // for this prototype 10 digits long number should be enough
    private static final long ACCOUNT_NUMBER_MIN = 1_000_000_000L;
    private static final long ACCOUNT_NUMBER_MAX = 9_999_999_999L;

    /**
     * Account number (ID) is stored as a 10-digits long number
     */
    @JsonValue
    private final long number;

    private AccountNumber(long number) {
        this.number = number;
    }

    /**
     * Static factory method to instantiate AccountNumber from the raw long number
     *
     * @param number account number
     * @return AccountNumber object
     */
    public static AccountNumber of(long number) {
        if (number < ACCOUNT_NUMBER_MIN || number > ACCOUNT_NUMBER_MAX) {
            throw new InvalidAccountNumberException("Invalid account number: " + number, String.valueOf(number));
        }
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

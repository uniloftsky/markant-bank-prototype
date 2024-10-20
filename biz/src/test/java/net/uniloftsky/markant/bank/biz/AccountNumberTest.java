package net.uniloftsky.markant.bank.biz;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class AccountNumberTest {

    @Test
    public void testCreateValidAccountNumber() {
        long number = 1234567890L;
        AccountNumber result = AccountNumber.of(number);
        assertNotNull(result);
        assertEquals(number, result.getNumber());
    }

    @Test
    public void testCreateInvalidAccountNumber() {
        long number = 1234L;
        try {
            AccountNumber.of(number);
        } catch (IllegalArgumentException ex) {
            assertNotNull(ex);
        }
    }

}

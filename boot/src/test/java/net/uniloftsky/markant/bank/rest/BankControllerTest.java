package net.uniloftsky.markant.bank.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.uniloftsky.markant.bank.biz.AccountNumber;
import net.uniloftsky.markant.bank.biz.BankAccount;
import net.uniloftsky.markant.bank.biz.BankServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.math.BigDecimal;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(BankController.class)
public class BankControllerTest {

    /**
     * BankController endpoints. "%s" is an account number path variable
     */
    private static final String GET_ACCOUNT = "/banking/accounts/%s";
    private static final String DEPOSIT = "/banking/accounts/%s/deposits";
    private static final String WITHDRAW = "/banking/accounts/%s/withdrawals";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BankServiceImpl bankService;

    /**
     * Object mapper to convert object to JSON
     */
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Dummy number
     */
    private final long number = 1234567890L;

    /**
     * Dummy account number
     */
    private final AccountNumber accountNumber = AccountNumber.of(number);

    /**
     * Dummy amount of money for balance or deposit/withdrawal transactions
     */
    private final BigDecimal amount = new BigDecimal("100.00");

    @Test
    public void testGetAccount() throws Exception {

        // given
        // mocking a bank service to return BankAccount object
        BankAccount bankAccount = new BankAccount(accountNumber, amount);
        given(bankService.getAccount(AccountNumber.of(number))).willReturn(bankAccount);

        // when
        String formattedEndpoint = String.format(GET_ACCOUNT, number);
        ResultActions result = mockMvc.perform(get(formattedEndpoint));

        // then
        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$").isNotEmpty());
        result.andExpect(jsonPath("$.number").value(number));
        result.andExpect(jsonPath("$.balance").value(amount.toPlainString()));
    }

    @Test
    public void testDeposit() throws Exception {

        // given
        // mocking bank service to return updated bank account object
        BankAccount bankAccount = new BankAccount(accountNumber, amount); // let's assume account balance equals "amount" after the deposit
        given(bankService.deposit(accountNumber, amount)).willReturn(bankAccount);

        // mocking a request payload
        BalanceUpdateRequest request = new BalanceUpdateRequest();
        request.setAmount(amount.toPlainString());
        String payload = objectMapper.writeValueAsString(request);

        // when
        String formattedEndpoint = String.format(DEPOSIT, number);
        ResultActions result = mockMvc.perform(
                post(formattedEndpoint)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload)
        );

        // then
        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$").isNotEmpty());
        result.andExpect(jsonPath("$.number").value(number));
        result.andExpect(jsonPath("$.balance").value(bankAccount.getBalance().toPlainString()));
    }

    @Test
    public void testWithdraw() throws Exception {

        // given
        // mocking bank service to return updated bank account object
        BankAccount bankAccount = new BankAccount(accountNumber, amount); // let's assume account balance equals "amount" after the withdrawal
        given(bankService.withdraw(accountNumber, amount)).willReturn(bankAccount);

        // mocking a request payload
        BalanceUpdateRequest request = new BalanceUpdateRequest();
        request.setAmount(amount.toPlainString());
        String payload = objectMapper.writeValueAsString(request);

        // when
        String formattedEndpoint = String.format(WITHDRAW, number);
        ResultActions result = mockMvc.perform(
                post(formattedEndpoint)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload)
        );

        // then
        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$").isNotEmpty());
        result.andExpect(jsonPath("$.number").value(number));
        result.andExpect(jsonPath("$.balance").value(bankAccount.getBalance().toPlainString()));
    }
}

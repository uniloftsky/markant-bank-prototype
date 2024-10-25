package net.uniloftsky.markant.bank.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.uniloftsky.markant.bank.biz.*;
import net.uniloftsky.markant.bank.config.JacksonConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(BankController.class)
@Import(JacksonConfig.class)
public class BankControllerTest {

    /**
     * BankController endpoints. "%s" is an account number path variable
     */
    private static final String GET_ACCOUNT = "/accounts/%s";
    private static final String DEPOSIT = "/accounts/%s/transactions/deposits";
    private static final String WITHDRAW = "/accounts/%s/transactions/withdrawals";
    private static final String TRANSFER = "/accounts/%s/transactions/transfers";
    private static final String TRANSACTIONS = "/accounts/%s/transactions";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JacksonConfig jacksonConfig;

    @MockBean
    private BankServiceImpl bankService;

    /**
     * Object mapper to convert object to JSON
     */
    private ObjectMapper objectMapper;

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

    @BeforeEach
    void setUp() {
        objectMapper = jacksonConfig.objectMapper();
    }

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
        result.andExpect(status().isCreated());
        result.andExpect(jsonPath("$").isNotEmpty());
        result.andExpect(jsonPath("$.number").value(number));
        result.andExpect(jsonPath("$.balance").value(bankAccount.getBalance().toPlainString()));
    }

    @Test
    public void testListDeposits() throws Exception {

        // given
        // mock bank service to return list of deposits
        TransactionId depositId = TransactionId.generateNew();
        BigDecimal amount = new BigDecimal("100");
        Instant timestamp = Instant.ofEpochMilli(System.currentTimeMillis());
        DepositTransaction deposit = new DepositTransaction(depositId, accountNumber, amount, timestamp);
        List<DepositTransaction> deposits = List.of(deposit);
        given(bankService.listDeposits(accountNumber)).willReturn(deposits);

        // when
        String formattedEndpoint = String.format(DEPOSIT, number);
        ResultActions result = mockMvc.perform(get(formattedEndpoint));

        // then
        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$").isNotEmpty());
        result.andExpect(jsonPath("$").isArray());
        result.andExpect(jsonPath("$[0].id").value(depositId.toString()));
        result.andExpect(jsonPath("$[0].amount").value(amount.toPlainString()));
        result.andExpect(jsonPath("$[0].timestamp").value(timestamp.toEpochMilli()));
        result.andExpect(jsonPath("$[0].accountNumber").value(number));
        result.andExpect(jsonPath("$[0].type").value(TransactionType.DEPOSIT.name()));
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
        result.andExpect(status().isCreated());
        result.andExpect(jsonPath("$").isNotEmpty());
        result.andExpect(jsonPath("$.number").value(number));
        result.andExpect(jsonPath("$.balance").value(bankAccount.getBalance().toPlainString()));
    }

    @Test
    public void testListWithdrawals() throws Exception {

        // given
        // mock bank service to return list of withdrawals
        TransactionId withdrawalId = TransactionId.generateNew();
        BigDecimal amount = new BigDecimal("100");
        Instant timestamp = Instant.ofEpochMilli(System.currentTimeMillis());
        WithdrawTransaction withdrawal = new WithdrawTransaction(withdrawalId, accountNumber, amount, timestamp);
        List<WithdrawTransaction> withdrawals = List.of(withdrawal);
        given(bankService.listWithdrawals(accountNumber)).willReturn(withdrawals);

        // when
        String formattedEndpoint = String.format(WITHDRAW, number);
        ResultActions result = mockMvc.perform(get(formattedEndpoint));

        // then
        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$").isNotEmpty());
        result.andExpect(jsonPath("$").isArray());
        result.andExpect(jsonPath("$[0].id").value(withdrawalId.toString()));
        result.andExpect(jsonPath("$[0].amount").value(amount.toPlainString()));
        result.andExpect(jsonPath("$[0].timestamp").value(timestamp.toEpochMilli()));
        result.andExpect(jsonPath("$[0].accountNumber").value(number));
        result.andExpect(jsonPath("$[0].type").value(TransactionType.WITHDRAWAL.name()));
    }

    @Test
    public void testTransfer() throws Exception {

        // given
        // mock bank service to return created transfer transaction
        TransactionId id = TransactionId.generateNew();
        BigDecimal amount = new BigDecimal("100");
        long targetNumber = 9999999999L;
        AccountNumber targetAccountNumber = AccountNumber.of(targetNumber);
        Instant timestamp = Instant.ofEpochMilli(System.currentTimeMillis());
        TransferTransaction transfer = new TransferTransaction(id, accountNumber, targetAccountNumber, amount, timestamp);
        given(bankService.transfer(accountNumber, targetAccountNumber, amount)).willReturn(transfer);

        TransferRequest request = new TransferRequest();
        request.setAmount(amount.toPlainString());
        request.setTargetAccountNumber(targetNumber);

        String json = objectMapper.writeValueAsString(request);

        // when
        String formattedEndpoint = String.format(TRANSFER, number);
        ResultActions result = mockMvc.perform(post(formattedEndpoint)
                .contentType(MediaType.APPLICATION_JSON).content(json));

        // then
        result.andExpect(status().isCreated());
        result.andExpect(jsonPath("$").isNotEmpty());
        result.andExpect(jsonPath("$.id").value(id.toString()));
        result.andExpect(jsonPath("$.amount").value(amount.toPlainString()));
        result.andExpect(jsonPath("$.timestamp").value(timestamp.toEpochMilli()));
        result.andExpect(jsonPath("$.fromAccountNumber").value(number));
        result.andExpect(jsonPath("$.toAccountNumber").value(targetAccountNumber.toString()));
        result.andExpect(jsonPath("$.type").value(TransactionType.TRANSFER.name()));
    }

    @Test
    public void testListTransfers() throws Exception {

        // given
        // mock bank service to return list of transfers
        TransactionId id = TransactionId.generateNew();
        BigDecimal amount = new BigDecimal("100");
        long targetNumber = 9999999999L;
        AccountNumber targetAccountNumber = AccountNumber.of(targetNumber);
        Instant timestamp = Instant.ofEpochMilli(System.currentTimeMillis());
        TransferTransaction transfer = new TransferTransaction(id, accountNumber, targetAccountNumber, amount, timestamp);
        List<TransferTransaction> transfers = List.of(transfer);
        given(bankService.listTransfers(accountNumber)).willReturn(transfers);

        // when
        String formattedEndpoint = String.format(TRANSFER, number);
        ResultActions result = mockMvc.perform(get(formattedEndpoint));

        // then
        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$").isNotEmpty());
        result.andExpect(jsonPath("$").isArray());
        result.andExpect(jsonPath("$[0].id").value(id.toString()));
        result.andExpect(jsonPath("$[0].amount").value(amount.toPlainString()));
        result.andExpect(jsonPath("$[0].timestamp").value(timestamp.toEpochMilli()));
        result.andExpect(jsonPath("$[0].fromAccountNumber").value(number));
        result.andExpect(jsonPath("$[0].toAccountNumber").value(targetNumber));
        result.andExpect(jsonPath("$[0].type").value(TransactionType.TRANSFER.name()));
    }

    @Test
    public void testListTransactions() throws Exception {

        // mock bank service to return list of all transactions
        List<BankTransaction> transactions = new ArrayList<>();
        given(bankService.listTransactions(accountNumber)).willReturn(transactions);

        // withdrawal transaction
        TransactionId withdrawalId = TransactionId.generateNew();
        BigDecimal withdrawalAmount = new BigDecimal("100");
        Instant withdrawalInstant = Instant.ofEpochMilli(System.currentTimeMillis());
        WithdrawTransaction withdrawal = new WithdrawTransaction(withdrawalId, accountNumber, withdrawalAmount, withdrawalInstant);
        transactions.add(withdrawal);

        // deposit transaction
        TransactionId depositId = TransactionId.generateNew();
        BigDecimal depositAmount = new BigDecimal("150");
        Instant depositTimestamp = Instant.ofEpochMilli(System.currentTimeMillis());
        DepositTransaction deposit = new DepositTransaction(depositId, accountNumber, depositAmount, depositTimestamp);
        transactions.add(deposit);

        // when
        String formattedEndpoint = String.format(TRANSACTIONS, number);
        ResultActions result = mockMvc.perform(get(formattedEndpoint));

        // then
        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$").isNotEmpty());
        result.andExpect(jsonPath("$").isArray());

        // testing first element in response
        result.andExpect(jsonPath("$[0].id").value(withdrawalId.toString()));
        result.andExpect(jsonPath("$[0].amount").value(withdrawalAmount.toPlainString()));
        result.andExpect(jsonPath("$[0].timestamp").value(withdrawalInstant.toEpochMilli()));
        result.andExpect(jsonPath("$[0].accountNumber").value(number));
        result.andExpect(jsonPath("$[0].type").value(TransactionType.WITHDRAWAL.name()));

        // testing second element in response
        result.andExpect(jsonPath("$[1].id").value(depositId.toString()));
        result.andExpect(jsonPath("$[1].amount").value(depositAmount.toPlainString()));
        result.andExpect(jsonPath("$[1].timestamp").value(depositTimestamp.toEpochMilli()));
        result.andExpect(jsonPath("$[1].accountNumber").value(number));
        result.andExpect(jsonPath("$[1].type").value(TransactionType.DEPOSIT.name()));
    }
}

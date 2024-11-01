package net.uniloftsky.markant.bank.biz.persistence;

import net.uniloftsky.markant.bank.biz.persistence.repository.AccountRepository;
import net.uniloftsky.markant.bank.biz.persistence.repository.DepositTransactionRepository;
import net.uniloftsky.markant.bank.biz.persistence.repository.TransferTransactionRepository;
import net.uniloftsky.markant.bank.biz.persistence.repository.WithdrawTransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class BankPersistenceServiceImplTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private DepositTransactionRepository depositTransactionRepository;

    @Mock
    private WithdrawTransactionRepository withdrawTransactionRepository;

    @Mock
    private TransferTransactionRepository transferTransactionRepository;

    @InjectMocks
    private BankPersistenceServiceImpl bankPersistenceService;

    /**
     * Dummy number
     */
    private long accountNumber;

    @BeforeEach
    void setUp() {
        accountNumber = 1234567890L;
    }

    @Test
    public void testCreateAccount() {

        // given
        long creationTimestamp = System.currentTimeMillis();
        String balance = "100.50";

        // mocking the repository to return created account
        AccountEntity savedEntity = new AccountEntity();
        savedEntity.setNumber(accountNumber);
        savedEntity.setCreatedAt(creationTimestamp);
        given(accountRepository.save(argThat(e -> // check that account is saved with right properties
                e.getNumber() == accountNumber &&
                        e.getCreatedAt() == creationTimestamp &&
                        e.getBalance().equals(balance))))
                .willReturn(savedEntity);

        // when
        AccountEntity result = bankPersistenceService.createAccount(accountNumber, balance, creationTimestamp);

        // then
        assertNotNull(result);
        assertEquals(accountNumber, result.getNumber());
        assertEquals(creationTimestamp, result.getCreatedAt());
    }

    @Test
    public void testGetAccount() {

        // given
        // mocking the repository to return account
        AccountEntity entity = new AccountEntity();
        entity.setNumber(accountNumber);
        String balance = "100.50";
        entity.setBalance(balance);
        given(accountRepository.findById(accountNumber)).willReturn(Optional.of(entity));

        // when
        Optional<AccountEntity> result = bankPersistenceService.getAccount(accountNumber);

        // then
        assertNotNull(result);
        assertTrue(result.isPresent());
        assertEquals(accountNumber, result.get().getNumber());
        assertEquals(balance, result.get().getBalance());
    }

    @Test
    public void testGetAccountDoesntExist() {

        // given
        // mocking the repository to return empty optional of account
        given(accountRepository.findById(accountNumber)).willReturn(Optional.empty());

        // when
        Optional<AccountEntity> result = bankPersistenceService.getAccount(accountNumber);

        // then
        assertTrue(result.isEmpty());
    }

    @Test
    public void testUpdateAccountBalance() {

        // given
        // mocking repository to return updated entity
        AccountEntity entity = new AccountEntity();
        entity.setNumber(accountNumber);
        String balance = "0";
        entity.setBalance(balance);
        given(accountRepository.save(entity)).willReturn(entity);

        // when
        String newBalance = "100";
        long updateTimestamp = System.currentTimeMillis();
        AccountEntity result = bankPersistenceService.updateAccountBalance(entity, newBalance, updateTimestamp);

        // then
        assertNotNull(result);
        assertEquals(accountNumber, result.getNumber());
        assertEquals(newBalance, result.getBalance());
        assertEquals(updateTimestamp, result.getUpdatedAt());
    }

    @Test
    public void testCreateDepositTransaction() {

        // given
        UUID id = UUID.randomUUID();
        long accountNumber = 1234567890L;
        String amount = "100.50";
        long timestamp = System.currentTimeMillis();

        // mock repository to return saved deposit transaction
        DepositTransactionEntity savedEntity = new DepositTransactionEntity();
        savedEntity.setId(id);
        savedEntity.setAccountNumber(accountNumber);
        savedEntity.setAmount(amount);
        savedEntity.setTimestamp(timestamp);
        given(depositTransactionRepository.save(argThat(e -> // test that deposit is saved with right properties
                e.getId().equals(id) &&
                        e.getAmount().equals(amount) &&
                        e.getAccountNumber() == accountNumber &&
                        e.getTimestamp() == timestamp
        ))).willReturn(savedEntity);

        // when
        DepositTransactionEntity result = bankPersistenceService.createDepositTransaction(id, accountNumber, amount, timestamp);

        // then
        assertNotNull(result);
        assertEquals(id, result.getId());
        assertEquals(accountNumber, result.getAccountNumber());
        assertEquals(amount, result.getAmount());
        assertEquals(timestamp, result.getTimestamp());
    }

    @Test
    public void testListDeposits() {

        // given
        // mocking repository to return list of deposits
        DepositTransactionEntity entity = new DepositTransactionEntity();
        entity.setId(UUID.randomUUID());
        List<DepositTransactionEntity> entities = List.of(entity);
        given(depositTransactionRepository.findAllByAccountNumber(accountNumber)).willReturn(entities);

        // when
        List<DepositTransactionEntity> result = bankPersistenceService.listDeposits(accountNumber);

        // then
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(entities.size(), result.size());
        assertEquals(entity.getId(), result.getFirst().getId());
    }

    @Test
    public void testCreateWithdrawTransaction() {

        // given
        UUID id = UUID.randomUUID();
        long accountNumber = 1234567890L;
        String amount = "100.50";
        long timestamp = System.currentTimeMillis();

        // mock repository to return saved withdrawal transaction
        WithdrawTransactionEntity savedEntity = new WithdrawTransactionEntity();
        savedEntity.setId(id);
        savedEntity.setAccountNumber(accountNumber);
        savedEntity.setAmount(amount);
        savedEntity.setTimestamp(timestamp);
        given(withdrawTransactionRepository.save(argThat(e -> // test that withdrawal is saved with right properties
                e.getId().equals(id) &&
                        e.getAmount().equals(amount) &&
                        e.getAccountNumber() == accountNumber &&
                        e.getTimestamp() == timestamp
        ))).willReturn(savedEntity);

        // when
        WithdrawTransactionEntity result = bankPersistenceService.createWithdrawTransaction(id, accountNumber, amount, timestamp);

        // then
        assertNotNull(result);
        assertEquals(id, result.getId());
        assertEquals(accountNumber, result.getAccountNumber());
        assertEquals(amount, result.getAmount());
        assertEquals(timestamp, result.getTimestamp());
    }

    @Test
    public void testListWithdrawals() {

        // given
        // mocking repository to return list of withdrawals
        WithdrawTransactionEntity entity = new WithdrawTransactionEntity();
        entity.setId(UUID.randomUUID());
        List<WithdrawTransactionEntity> entities = List.of(entity);
        given(withdrawTransactionRepository.findAllByAccountNumber(accountNumber)).willReturn(entities);

        // when
        List<WithdrawTransactionEntity> result = bankPersistenceService.listWithdrawals(accountNumber);

        // then
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(entities.size(), result.size());
        assertEquals(entity.getId(), result.getFirst().getId());
    }

    @Test
    public void testCreateTransferTransaction() {

        // given
        UUID id = UUID.randomUUID();
        long toAccountNumber = 1234567899L;
        String amount = "100";
        long timestamp = System.currentTimeMillis();

        // mock repository to return created transfer transaction entity
        TransferTransactionEntity entity = new TransferTransactionEntity();
        entity.setId(id);
        entity.setFromAccountNumber(accountNumber);
        entity.setToAccountNumber(toAccountNumber);
        entity.setAmount(amount);
        entity.setTimestamp(timestamp);
        given(transferTransactionRepository.save(argThat(e -> // test that transfer is saved with right properties
                e.getId().equals(id) &&
                        e.getFromAccountNumber() == accountNumber &&
                        e.getToAccountNumber() == toAccountNumber &&
                        e.getAmount().equals(amount) &&
                        e.getTimestamp() == timestamp))).willReturn(entity);

        // when
        TransferTransactionEntity result = bankPersistenceService.createTransferTransaction(id, accountNumber, toAccountNumber, amount, timestamp);

        // then
        assertNotNull(result);
        assertEquals(id, result.getId());
        assertEquals(accountNumber, result.getFromAccountNumber());
        assertEquals(toAccountNumber, result.getToAccountNumber());
        assertEquals(amount, result.getAmount());
        assertEquals(timestamp, result.getTimestamp());
    }

    @Test
    public void testListTransfers() {

        // given
        // mock repository to return list of transfers
        TransferTransactionEntity entity = new TransferTransactionEntity();
        entity.setId(UUID.randomUUID());
        List<TransferTransactionEntity> entities = List.of(entity);
        given(transferTransactionRepository.findAllByFromOrToAccountNumber(accountNumber)).willReturn(entities);

        // when
        List<TransferTransactionEntity> result = bankPersistenceService.listTransfers(accountNumber);

        // then
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(entities.size(), result.size());
        assertEquals(entity.getId(), result.getFirst().getId());
    }
}

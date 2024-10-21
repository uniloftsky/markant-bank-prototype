package net.uniloftsky.markant.bank.biz.persistence;

import net.uniloftsky.markant.bank.biz.persistence.repository.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static net.uniloftsky.markant.bank.biz.persistence.BankPersistenceServiceImpl.INITIAL_BALANCE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class BankPersistenceServiceImplTest {

    @Mock
    private AccountRepository accountRepository;

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

        // mocking the repository to return created account
        AccountEntity savedEntity = new AccountEntity();
        savedEntity.setNumber(accountNumber);
        savedEntity.setCreatedAt(creationTimestamp);
        given(accountRepository.save(argThat(e -> // check that account is saved with right properties
                e.getNumber() == accountNumber &&
                        e.getCreatedAt() == creationTimestamp &&
                        e.getBalance().equals(INITIAL_BALANCE))))
                .willReturn(savedEntity);

        // when
        AccountEntity result = bankPersistenceService.createAccount(accountNumber, creationTimestamp);

        // then
        assertNotNull(result);
        assertEquals(accountNumber, result.getNumber());
        assertEquals(creationTimestamp, result.getCreatedAt());
    }

    @Test
    public void testCreateAccountInvalidNumber() {

        // given
        long creationTimestamp = System.currentTimeMillis();

        try {

            // when
            bankPersistenceService.createAccount(0, creationTimestamp);
        } catch (IllegalArgumentException ex) {

            // then
            assertNotNull(ex);
        }
    }

    @Test
    public void testCreateAccountInvalidCreationTimestamp() {
        try {

            // when
            bankPersistenceService.createAccount(accountNumber, 0);
        } catch (IllegalArgumentException ex) {

            // then
            assertNotNull(ex);
        }
    }

    @Test
    public void testGetAccount() {

        // given
        // mocking the repository to return account
        AccountEntity entity = new AccountEntity();
        entity.setNumber(accountNumber);
        entity.setBalance(INITIAL_BALANCE);
        given(accountRepository.findById(accountNumber)).willReturn(Optional.of(entity));

        // when
        AccountEntity result = bankPersistenceService.getAccount(accountNumber);

        // then
        assertNotNull(result);
        assertEquals(accountNumber, result.getNumber());
        assertEquals(INITIAL_BALANCE, result.getBalance());
    }

    @Test
    public void testGetAccountDoesntExist() {

        // given
        // mocking the repository to empty optional of account
        given(accountRepository.findById(accountNumber)).willReturn(Optional.empty());

        try {

            // when
            bankPersistenceService.getAccount(accountNumber);
        } catch (AccountNotFoundPersistenceServiceException ex) {

            // then
            assertNotNull(ex);
        }
    }

    @Test
    public void testUpdateAccountBalance() {

        // given
        // mocking repository to return updated entity
        AccountEntity entity = new AccountEntity();
        entity.setNumber(accountNumber);
        entity.setBalance(INITIAL_BALANCE);
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
}

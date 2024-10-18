package net.uniloftsky.markant.bank.biz.persistence;

import net.uniloftsky.markant.bank.biz.BankAccount;
import net.uniloftsky.markant.bank.biz.persistence.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class BankPersistenceServiceImpl implements BankPersistenceService {

    private AccountRepository accountRepository;

    @Override
    @Transactional
    public AccountEntity createAccount(BankAccount.AccountId accountId, long creationTimestamp) {
        if (accountId == null) {
            throw new IllegalArgumentException("accountId cannot be null");
        }
        if (creationTimestamp <= 0) {
            throw new IllegalArgumentException("creationTimestamp cannot be negative or zero");
        }

        AccountEntity account = new AccountEntity();
        account.setId(accountId.toString());
        account.setBalance(0);
        account.setCreatedAt(creationTimestamp);
        account = accountRepository.save(account);
        return account;
    }

    @Override
    @Transactional(readOnly = true)
    public AccountEntity getAccount(BankAccount.AccountId accountId) throws BankPersistenceServiceException {
        if (accountId == null) {
            throw new IllegalArgumentException("accountId cannot be null");
        }

        Optional<AccountEntity> accountOptional = accountRepository.findById(accountId.toString());
        if (accountOptional.isEmpty()) {
            throw new AccountNotFoundPersistenceServiceException(accountId.toString());
        }

        return accountOptional.get();
    }

    @Autowired
    public void setAccountRepository(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }
}

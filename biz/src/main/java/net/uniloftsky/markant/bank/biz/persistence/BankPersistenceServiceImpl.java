package net.uniloftsky.markant.bank.biz.persistence;

import net.uniloftsky.markant.bank.biz.persistence.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BankPersistenceServiceImpl implements BankPersistenceService {

    private AccountRepository accountRepository;

    @Override
    public AccountEntity createAccount(long accountNumber, String balance, long creationTimestamp) {
        assert accountNumber >= 0 && balance != null && !balance.isEmpty() && creationTimestamp >= 0;

        AccountEntity account = new AccountEntity();
        account.setNumber(accountNumber);
        account.setBalance(balance);
        account.setCreatedAt(creationTimestamp);
        account = accountRepository.save(account);
        return account;
    }

    @Override
    public AccountEntity getAccount(long accountNumber) {
        assert accountNumber >= 0;

        Optional<AccountEntity> accountOptional = accountRepository.findById(accountNumber);
        if (accountOptional.isEmpty()) {
            throw new AccountNotFoundPersistenceServiceException("account with number " + accountNumber + " not found");
        }
        return accountOptional.get();
    }

    @Override
    public AccountEntity updateAccountBalance(AccountEntity accountEntity, String newBalance, long timestamp) {
        assert accountEntity != null && newBalance != null && !newBalance.isEmpty() && timestamp >= 0;

        accountEntity.setBalance(newBalance);
        accountEntity.setUpdatedAt(timestamp);
        accountEntity = accountRepository.save(accountEntity);
        return accountEntity;
    }

    @Autowired
    public void setAccountRepository(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }
}

package net.uniloftsky.markant.bank.biz.persistence;

import net.uniloftsky.markant.bank.biz.persistence.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class BankPersistenceServiceImpl implements BankPersistenceService {

    private AccountRepository accountRepository;

    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public AccountEntity createAccount(int accountNumber, long creationTimestamp) {
        if (accountNumber == 0) {
            throw new IllegalArgumentException("accountId cannot be null");
        }
        if (creationTimestamp <= 0) {
            throw new IllegalArgumentException("creationTimestamp cannot be negative or zero");
        }

        AccountEntity account = new AccountEntity();
        account.setId(accountNumber);
        account.setBalance(0);
        account.setCreatedAt(creationTimestamp);
        account = accountRepository.save(account);
        return account;
    }

    @Override
    @Transactional(readOnly = true)
    public AccountEntity getAccount(int accountNumber) throws AccountNotFoundPersistenceServiceException {


        Optional<AccountEntity> accountOptional = accountRepository.findById(accountNumber);
        if (accountOptional.isEmpty()) {
            throw new AccountNotFoundPersistenceServiceException("account with account number " + accountNumber + " not found");
        }

        return accountOptional.get();
    }

    @Override
    public AccountEntity updateAccountBalance(int accountNumber, double newBalance, long timestamp) {
        return null;
    }

    @Autowired
    public void setAccountRepository(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }
}

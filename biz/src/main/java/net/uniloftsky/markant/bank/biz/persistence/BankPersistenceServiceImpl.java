package net.uniloftsky.markant.bank.biz.persistence;

import net.uniloftsky.markant.bank.biz.persistence.repository.AccountRepository;
import net.uniloftsky.markant.bank.biz.persistence.repository.DepositTransactionRepository;
import net.uniloftsky.markant.bank.biz.persistence.repository.TransferTransactionRepository;
import net.uniloftsky.markant.bank.biz.persistence.repository.WithdrawTransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class BankPersistenceServiceImpl implements BankPersistenceService {

    private AccountRepository accountRepository;
    private DepositTransactionRepository depositRepository;
    private WithdrawTransactionRepository withdrawRepository;
    private TransferTransactionRepository transferRepository;

    @Override
    public AccountEntity createAccount(long accountNumber, String balance, long creationTimestamp) {
        assert accountNumber > 0 && balance != null && !balance.isEmpty() && creationTimestamp > 0;

        AccountEntity account = new AccountEntity();
        account.setNumber(accountNumber);
        account.setBalance(balance);
        account.setCreatedAt(creationTimestamp);
        account = accountRepository.save(account);
        return account;
    }

    @Override
    public Optional<AccountEntity> getAccount(long accountNumber) {
        assert accountNumber > 0;

        return accountRepository.findById(accountNumber);
    }

    @Override
    public AccountEntity updateAccountBalance(AccountEntity accountEntity, String newBalance, long timestamp) {
        assert accountEntity != null && newBalance != null && !newBalance.isEmpty() && timestamp > 0;

        accountEntity.setBalance(newBalance);
        accountEntity.setUpdatedAt(timestamp);
        accountEntity = accountRepository.save(accountEntity);
        return accountEntity;
    }

    @Override
    public List<DepositTransactionEntity> listDeposits(long accountNumber) {
        assert accountNumber > 0;

        return new ArrayList<>(depositRepository.findAllByAccountNumber(accountNumber));
    }

    @Override
    public DepositTransactionEntity createDepositTransaction(UUID id, long accountNumber, String amount, long timestamp) {
        assert id != null && accountNumber > 0 && amount != null && !amount.isEmpty() && timestamp > 0;

        DepositTransactionEntity depositTransaction = new DepositTransactionEntity();
        depositTransaction.setId(id);
        depositTransaction.setAccountNumber(accountNumber);
        depositTransaction.setAmount(amount);
        depositTransaction.setTimestamp(timestamp);
        depositTransaction = depositRepository.save(depositTransaction);
        return depositTransaction;
    }

    @Override
    public List<WithdrawTransactionEntity> listWithdrawals(long accountNumber) {
        assert accountNumber > 0;

        return new ArrayList<>(withdrawRepository.findAllByAccountNumber(accountNumber));
    }

    @Override
    public WithdrawTransactionEntity createWithdrawTransaction(UUID id, long accountNumber, String amount, long timestamp) {
        assert id != null && accountNumber > 0 && amount != null && !amount.isEmpty() && timestamp > 0;

        WithdrawTransactionEntity withdrawTransaction = new WithdrawTransactionEntity();
        withdrawTransaction.setId(id);
        withdrawTransaction.setAccountNumber(accountNumber);
        withdrawTransaction.setAmount(amount);
        withdrawTransaction.setTimestamp(timestamp);
        withdrawTransaction = withdrawRepository.save(withdrawTransaction);
        return withdrawTransaction;
    }

    @Override
    public List<TransferTransactionEntity> listTransfers(long accountNumber) {
        assert accountNumber > 0;

        return new ArrayList<>(transferRepository.findAllByFromOrToAccountNumber(accountNumber));
    }

    @Override
    public TransferTransactionEntity createTransferTransaction(UUID id, long fromAccountNumber, long toAccountNumber, String amount, long timestamp) {
        assert id != null && fromAccountNumber > 0 && toAccountNumber > 0 && amount != null && !amount.isEmpty() && timestamp > 0;

        TransferTransactionEntity transferTransaction = new TransferTransactionEntity();
        transferTransaction.setId(id);
        transferTransaction.setFromAccountNumber(fromAccountNumber);
        transferTransaction.setToAccountNumber(toAccountNumber);
        transferTransaction.setAmount(amount);
        transferTransaction.setTimestamp(timestamp);
        transferTransaction = transferRepository.save(transferTransaction);
        return transferTransaction;
    }

    @Autowired
    public void setAccountRepository(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Autowired
    public void setDepositRepository(DepositTransactionRepository depositRepository) {
        this.depositRepository = depositRepository;
    }

    @Autowired
    public void setWithdrawRepository(WithdrawTransactionRepository withdrawRepository) {
        this.withdrawRepository = withdrawRepository;
    }

    @Autowired
    public void setTransferRepository(TransferTransactionRepository transferRepository) {
        this.transferRepository = transferRepository;
    }
}

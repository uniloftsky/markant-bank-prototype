package net.uniloftsky.markant.bank.rest;

import io.swagger.v3.oas.annotations.tags.Tag;
import net.uniloftsky.markant.bank.biz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Tag(name = "banking", description = "Bank API")
public class BankController {

    private BankService bankService;

    @GetMapping("accounts/{accountNumber}")
    public ResponseEntity<BankAccount> getAccount(@PathVariable("accountNumber") long accountNumber) throws AccountNotFoundException {
        BankAccount result = bankService.getAccount(AccountNumber.of(accountNumber));
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("accounts/{accountNumber}/transactions/deposits")
    public ResponseEntity<BankAccount> deposit(@PathVariable("accountNumber") long accountNumber, @RequestBody BalanceUpdateRequest request) {
        BankAccount result = bankService.deposit(AccountNumber.of(accountNumber), request.getAmount());
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @GetMapping("accounts/{accountNumber}/transactions/deposits")
    public ResponseEntity<List<DepositTransaction>> listDeposits(@PathVariable("accountNumber") long accountNumber) throws AccountNotFoundException {
        List<DepositTransaction> result = bankService.listDeposits(AccountNumber.of(accountNumber));
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("accounts/{accountNumber}/transactions/withdrawals")
    public ResponseEntity<BankAccount> withdraw(@PathVariable("accountNumber") long accountNumber, @RequestBody BalanceUpdateRequest request) throws InsufficientBalanceException, AccountNotFoundException {
        BankAccount result = bankService.withdraw(AccountNumber.of(accountNumber), request.getAmount());
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @GetMapping("accounts/{accountNumber}/transactions/withdrawals")
    public ResponseEntity<List<WithdrawTransaction>> listWithdrawals(@PathVariable("accountNumber") long accountNumber) throws AccountNotFoundException {
        List<WithdrawTransaction> result = bankService.listWithdrawals(AccountNumber.of(accountNumber));
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("accounts/{accountNumber}/transactions/transfers")
    public ResponseEntity<TransferTransaction> transfer(@PathVariable("accountNumber") long fromAccountNumber, @RequestBody TransferRequest request) {
        TransferTransaction result = bankService.transfer(AccountNumber.of(fromAccountNumber), request.getTargetAccountNumber(), request.getAmount());
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @GetMapping("accounts/{accountNumber}/transactions/transfers")
    public ResponseEntity<List<TransferTransaction>> listTransfers(@PathVariable("accountNumber") long accountNumber) throws AccountNotFoundException {
        List<TransferTransaction> result = bankService.listTransfers(AccountNumber.of(accountNumber));
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("accounts/{accountNumber}/transactions")
    public ResponseEntity<List<BankTransaction>> listTransactions(@PathVariable("accountNumber") long accountNumber) throws AccountNotFoundException {
        List<BankTransaction> result = bankService.listTransactions(AccountNumber.of(accountNumber));
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @Autowired
    public void setBankService(BankService bankService) {
        this.bankService = bankService;
    }

}

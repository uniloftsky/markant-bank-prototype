package net.uniloftsky.markant.bank.rest;

import io.swagger.v3.oas.annotations.tags.Tag;
import net.uniloftsky.markant.bank.biz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("banking")
@Tag(name = "banking", description = "Bank API")
public class BankController {

    private BankService bankService;

    @GetMapping("accounts/{accountNumber}")
    public ResponseEntity<BankAccount> getAccount(@PathVariable("accountNumber") long accountNumber) throws AccountNotFoundException {
        BankAccount result = bankService.getAccount(AccountNumber.of(accountNumber));
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("accounts/{accountNumber}/deposits")
    public ResponseEntity<BankAccount> deposit(@PathVariable("accountNumber") long accountNumber, @RequestBody BalanceUpdateRequest request) {
        BankAccount result = bankService.deposit(AccountNumber.of(accountNumber), new BigDecimal(request.getAmount()));
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("accounts/{accountNumber}/withdrawals")
    public ResponseEntity<BankAccount> withdraw(@PathVariable("accountNumber") long accountNumber, @RequestBody BalanceUpdateRequest request) throws InsufficientBalanceException, AccountNotFoundException {
        BankAccount result = bankService.withdraw(AccountNumber.of(accountNumber), new BigDecimal(request.getAmount()));
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @Autowired
    public void setBankService(BankService bankService) {
        this.bankService = bankService;
    }

}

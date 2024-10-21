package net.uniloftsky.markant.bank.rest;

import io.swagger.v3.oas.annotations.tags.Tag;
import net.uniloftsky.markant.bank.biz.AccountNotFoundException;
import net.uniloftsky.markant.bank.biz.AccountNumber;
import net.uniloftsky.markant.bank.biz.BankAccount;
import net.uniloftsky.markant.bank.biz.BankService;
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

    @GetMapping("account/{number}")
    public ResponseEntity<BankAccount> getAccount(@PathVariable("number") long accountNumber) throws AccountNotFoundException {
        BankAccount result = bankService.getAccount(AccountNumber.of(accountNumber));
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("account/deposit")
    public ResponseEntity<BankAccount> deposit(@RequestBody TransactionRequest request) {
        BankAccount result = bankService.deposit(AccountNumber.of(request.getNumber()), new BigDecimal(request.getAmount()));
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @Autowired
    public void setBankService(BankService bankService) {
        this.bankService = bankService;
    }

}

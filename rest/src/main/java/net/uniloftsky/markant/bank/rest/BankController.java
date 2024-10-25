package net.uniloftsky.markant.bank.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import net.uniloftsky.markant.bank.biz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Tag(name = "banking", description = "Bank API")
public class BankController {

    private BankService bankService;

    @GetMapping(value = "accounts/{accountNumber}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get banking account balance", description = "Endpoint to check the balance of the given banking account")
    @Parameter(name = "accountNumber", description = "Account number to get balance. Must be a 10 digits long number")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Request was performed successfully", content = @Content(schema = @Schema(implementation = BankAccount.class))),
            @ApiResponse(responseCode = "404", description = "If account by the given account number doesn't exist", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "400", description = "Bad request. Check the description in response", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<BankAccount> getAccount(@PathVariable("accountNumber") long accountNumber) {
        BankAccount result = bankService.getAccount(AccountNumber.of(accountNumber));
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping(value = "accounts/{accountNumber}/transactions/deposits", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Deposit money to the account", description = "Endpoint to deposit the specified amount of money to the given account")
    @Parameter(name = "accountNumber", description = "Account number where deposit to. Must be a 10 digits long number")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Request was performed successfully", content = @Content(schema = @Schema(implementation = BankAccount.class))),
            @ApiResponse(responseCode = "400", description = "Bad request. Check the description in response", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<BankAccount> deposit(@PathVariable("accountNumber") long accountNumber, @RequestBody BalanceUpdateRequest request) {
        BankAccount result = bankService.deposit(AccountNumber.of(accountNumber), request.getAmount());
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @GetMapping(value = "accounts/{accountNumber}/transactions/deposits", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get history of deposits for the account", description = "Endpoint to get history of deposits for the given account")
    @Parameter(name = "accountNumber", description = "Account number. Must be a 10 digits long number")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Request was performed successfully",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = DepositTransaction.class)))),
            @ApiResponse(responseCode = "404", description = "If account by the given account number doesn't exist", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "400", description = "Bad request. Check the description in response", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<List<DepositTransaction>> listDeposits(@PathVariable("accountNumber") long accountNumber) {
        List<DepositTransaction> result = bankService.listDeposits(AccountNumber.of(accountNumber));
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping(value = "accounts/{accountNumber}/transactions/withdrawals", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Withdrawal money from the account", description = "Endpoint to withdraw the specified amount of money from the given account")
    @Parameter(name = "accountNumber", description = "Account number where withdraw from. Must be a 10 digits long number")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Request was performed successfully", content = @Content(schema = @Schema(implementation = BankAccount.class))),
            @ApiResponse(responseCode = "404", description = "If account by the given account number doesn't exist", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "400", description = "Bad request. Check the description in response", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<BankAccount> withdraw(@PathVariable("accountNumber") long accountNumber, @RequestBody BalanceUpdateRequest request) {
        BankAccount result = bankService.withdraw(AccountNumber.of(accountNumber), request.getAmount());
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @GetMapping(value = "accounts/{accountNumber}/transactions/withdrawals", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get history of withdrawals for the account", description = "Endpoint to get history of withdrawals for the given account")
    @Parameter(name = "accountNumber", description = "Account number. Must be a 10 digits long number")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Request was performed successfully",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = WithdrawTransaction.class)))),
            @ApiResponse(responseCode = "404", description = "If account by the given account number doesn't exist",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "400", description = "Bad request. Check the description in response", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<List<WithdrawTransaction>> listWithdrawals(@PathVariable("accountNumber") long accountNumber) {
        List<WithdrawTransaction> result = bankService.listWithdrawals(AccountNumber.of(accountNumber));
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping(value = "accounts/{accountNumber}/transactions/transfers", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Transfer money from one account to another", description = "Endpoint to transfer the specified amount of money from one account to another")
    @Parameter(name = "accountNumber", description = "Account number, transfer initiator. Must be a 10 digits long number")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Request was performed successfully", content = @Content(schema = @Schema(implementation = TransferTransaction.class))),
            @ApiResponse(responseCode = "404", description = "If account by the given account number doesn't exist", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "400", description = "Bad request. Check the description in response", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<TransferTransaction> transfer(@PathVariable("accountNumber") long fromAccountNumber, @RequestBody TransferRequest request) {
        TransferTransaction result = bankService.transfer(AccountNumber.of(fromAccountNumber), request.getTargetAccountNumber(), request.getAmount());
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @GetMapping(value = "accounts/{accountNumber}/transactions/transfers", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get history of transfers for the account", description = "Endpoint to get history of transfers for the given account")
    @Parameter(name = "accountNumber", description = "Account number. Must be a 10 digits long number")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Request was performed successfully",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = TransferTransaction.class)))),
            @ApiResponse(responseCode = "404", description = "If account by the given account number doesn't exist", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "400", description = "Bad request. Check the description in response", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<List<TransferTransaction>> listTransfers(@PathVariable("accountNumber") long accountNumber) {
        List<TransferTransaction> result = bankService.listTransfers(AccountNumber.of(accountNumber));
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping(value = "accounts/{accountNumber}/transactions", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get history of all transactions for the account", description = "Endpoint to get history of transactions for the given account")
    @Parameter(name = "accountNumber", description = "Account number. Must be a 10 digits long number")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Request was performed successfully",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = BankTransaction.class)))),
            @ApiResponse(responseCode = "404", description = "If account by the given account number doesn't exist", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "400", description = "Bad request. Check the description in response", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<List<BankTransaction>> listTransactions(@PathVariable("accountNumber") long accountNumber) {
        List<BankTransaction> result = bankService.listTransactions(AccountNumber.of(accountNumber));
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @Autowired
    public void setBankService(BankService bankService) {
        this.bankService = bankService;
    }

}

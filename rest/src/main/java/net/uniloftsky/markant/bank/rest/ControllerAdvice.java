package net.uniloftsky.markant.bank.rest;

import net.uniloftsky.markant.bank.biz.AccountNotFoundException;
import net.uniloftsky.markant.bank.biz.InsufficientBalanceException;
import net.uniloftsky.markant.bank.biz.InvalidAccountNumberException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

/**
 * Controller advice to handle exceptions
 */
@RestControllerAdvice
public class ControllerAdvice {

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ErrorResponse> handleAPINotFound(NoHandlerFoundException ex) {
        ErrorResponse errorResponse = ErrorResponse.of(ErrorKey.API_NOT_FOUND_ERROR, "Requested API path doesn't exist");
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AccountNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleAccountNotFound(AccountNotFoundException ex) {
        ErrorResponse errorResponse = ErrorResponse.of(ErrorKey.ACCOUNT_NOT_FOUND, ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InsufficientBalanceException.class)
    public ResponseEntity<ErrorResponse> handleInsufficientBalance(InsufficientBalanceException ex) {
        ErrorResponse errorResponse = ErrorResponse.of(ErrorKey.INSUFFICIENT_BALANCE_ERROR, ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidAccountNumberException.class)
    public ResponseEntity<ErrorResponse> handleInvalidAccountNumber(InvalidAccountNumberException ex) {
        ErrorResponse errorResponse = ErrorResponse.of(ErrorKey.INVALID_ACCOUNT_NUMBER_ERROR, ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        ErrorResponse errorResponse = ErrorResponse.of(ErrorKey.SYSTEM_INTERNAL_ERROR, "Internal server error");
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}

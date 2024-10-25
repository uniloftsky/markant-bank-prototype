package net.uniloftsky.markant.bank.rest;

import net.uniloftsky.markant.bank.biz.AccountNotFoundException;
import net.uniloftsky.markant.bank.biz.InsufficientBalanceException;
import net.uniloftsky.markant.bank.biz.InvalidAccountNumberException;
import net.uniloftsky.markant.bank.biz.TransactionAmountFormatException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.Locale;

/**
 * Controller advice to handle exceptions
 */
@RestControllerAdvice
public class ControllerAdvice {

    private static final Logger logger = LoggerFactory.getLogger(ControllerAdvice.class);

    /**
     * Message source to retrieve localization
     */
    private MessageSource messageSource;

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ErrorResponse> handleAPINotFound() {
        ErrorResponse errorResponse = buildErrorResponse(ErrorKey.API_NOT_FOUND_ERROR);
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AccountNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleAccountNotFound(AccountNotFoundException ex) {
        ErrorResponse errorResponse = buildErrorResponse(ErrorKey.ACCOUNT_NOT_FOUND_ERROR, ex.getAccountNumber().toString());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InsufficientBalanceException.class)
    public ResponseEntity<ErrorResponse> handleInsufficientBalance() {
        ErrorResponse errorResponse = buildErrorResponse(ErrorKey.INSUFFICIENT_BALANCE_ERROR);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidAccountNumberException.class)
    public ResponseEntity<ErrorResponse> handleInvalidAccountNumber(InvalidAccountNumberException ex) {
        ErrorResponse errorResponse = buildErrorResponse(ErrorKey.INVALID_ACCOUNT_NUMBER_ERROR, ex.getAccountNumber());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        logger.error("Unhandled exception occurred", ex);
        ErrorResponse errorResponse = buildErrorResponse(ErrorKey.SYSTEM_INTERNAL_ERROR);
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(TransactionAmountFormatException.class)
    public ResponseEntity<ErrorResponse> handleTransactionAmountFormatException(TransactionAmountFormatException ex) {
        ErrorResponse errorResponse = buildErrorResponse(ErrorKey.TRANSACTION_AMOUNT_FORMAT_ERROR, ex.getAmount());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * Builds an error response object based on the provided error key and placeholder values.
     *
     * @param errorKey  the key representing the error
     * @param variables values to replace placeholders in the error message
     * @return an ErrorResponse object with the localized message
     */
    ErrorResponse buildErrorResponse(ErrorKey errorKey, String... variables) {
        String message = getLocalizedMessage(errorKey.getMessageKey(), variables);
        return ErrorResponse.of(errorKey, message);
    }

    /**
     * Retrieves a localized message based on the provided message key and replaces placeholders with given variables.
     *
     * @param messageKey the key corresponding to an entry in exceptions.properties
     * @param variables  values to replace placeholders, provided in the order they appear in the message
     * @return the localized message with placeholders replaced by the specified variables
     */
    String getLocalizedMessage(String messageKey, String... variables) {
        return messageSource.getMessage(messageKey, variables, Locale.ROOT);
    }

    @Autowired
    public void setMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
    }
}

package net.uniloftsky.markant.bank.rest;


import net.uniloftsky.markant.bank.biz.AccountNotFoundException;
import net.uniloftsky.markant.bank.biz.AccountNumber;
import net.uniloftsky.markant.bank.biz.InvalidAccountNumberException;
import net.uniloftsky.markant.bank.biz.TransactionAmountFormatException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
public class ControllerAdviceTest {

    @Mock
    private MessageSource messageSource;

    @Spy
    @InjectMocks
    private ControllerAdvice controllerAdvice;

    @Test
    public void testHandleAPINotFound() {

        // given
        ErrorKey errorKey = ErrorKey.API_NOT_FOUND_ERROR;
        String message = "message";

        // create a mocked error response object with specified error key and message
        mockErrorResponse(errorKey, message);

        // when
        ResponseEntity<ErrorResponse> result = controllerAdvice.handleAPINotFound();

        // then
        assertNotNull(result);
        assertNotNull(result.getBody());
        assertEquals(errorKey, result.getBody().getKey());
        assertEquals(message, result.getBody().getMessage());
    }

    @Test
    public void testHandleAccountNotFound() {

        // given
        ErrorKey errorKey = ErrorKey.ACCOUNT_NOT_FOUND_ERROR;
        AccountNumber accountNumber = AccountNumber.of(1234567890L);
        AccountNotFoundException ex = new AccountNotFoundException(accountNumber);
        String message = "message";

        // create a mocked error response object with specified error key, message and placeholder variables
        mockErrorResponse(errorKey, message, accountNumber.toString());

        // when
        ResponseEntity<ErrorResponse> result = controllerAdvice.handleAccountNotFound(ex);

        // then
        assertNotNull(result);
        assertNotNull(result.getBody());
        assertEquals(errorKey, result.getBody().getKey());
        assertEquals(message, result.getBody().getMessage());
    }

    @Test
    public void testHandleInsufficientBalance() {

        // given
        ErrorKey errorKey = ErrorKey.INSUFFICIENT_BALANCE_ERROR;
        String message = "message";

        // create a mocked error response object with specified error key, message and placeholder variables
        mockErrorResponse(errorKey, message);

        // when
        ResponseEntity<ErrorResponse> result = controllerAdvice.handleInsufficientBalance();

        // then
        assertNotNull(result);
        assertNotNull(result.getBody());
        assertEquals(errorKey, result.getBody().getKey());
        assertEquals(message, result.getBody().getMessage());
    }

    @Test
    public void testHandleInvalidAccountNumber() {

        // given
        ErrorKey errorKey = ErrorKey.INVALID_ACCOUNT_NUMBER_ERROR;
        String invalidAccountNumber = "1234567890";
        InvalidAccountNumberException ex = new InvalidAccountNumberException(invalidAccountNumber);
        String message = "message";

        // create a mocked error response object with specified error key, message and placeholder variables
        mockErrorResponse(errorKey, message, invalidAccountNumber);

        // when
        ResponseEntity<ErrorResponse> result = controllerAdvice.handleInvalidAccountNumber(ex);

        // then
        assertNotNull(result);
        assertNotNull(result.getBody());
        assertEquals(errorKey, result.getBody().getKey());
        assertEquals(message, result.getBody().getMessage());
    }

    @Test
    public void testHandleGenericException() {

        // given
        ErrorKey errorKey = ErrorKey.SYSTEM_INTERNAL_ERROR;
        Exception ex = new Exception();
        String message = "message";

        // create a mocked error response object with specified error key, message and placeholder variables
        mockErrorResponse(errorKey, message);

        // when
        ResponseEntity<ErrorResponse> result = controllerAdvice.handleGenericException(ex);

        // then
        assertNotNull(result);
        assertNotNull(result.getBody());
        assertEquals(errorKey, result.getBody().getKey());
        assertEquals(message, result.getBody().getMessage());
    }

    @Test
    public void testHandleTransactionAmountFormatException() {

        // given
        ErrorKey errorKey = ErrorKey.TRANSACTION_AMOUNT_FORMAT_ERROR;
        String invalidAmount = "100,50";
        TransactionAmountFormatException ex = new TransactionAmountFormatException(invalidAmount);
        String message = "message";

        // create a mocked error response object with specified error key, message and placeholder variables
        mockErrorResponse(errorKey, message, invalidAmount);

        // when
        ResponseEntity<ErrorResponse> result = controllerAdvice.handleTransactionAmountFormatException(ex);

        // then
        assertNotNull(result);
        assertNotNull(result.getBody());
        assertEquals(errorKey, result.getBody().getKey());
        assertEquals(message, result.getBody().getMessage());
    }

    @Test
    public void testBuildErrorResponse() {

        // given
        ErrorKey errorKey = ErrorKey.SYSTEM_INTERNAL_ERROR;
        String[] variables = new String[]{"var1", "var2"};

        String mockedLocalizedMessage = "message";
        doReturn(mockedLocalizedMessage).when(controllerAdvice).getLocalizedMessage(errorKey.getMessageKey(), variables);

        // when
        ErrorResponse response = controllerAdvice.buildErrorResponse(errorKey, variables);

        // then
        assertNotNull(response);
        assertEquals(errorKey, response.getKey());
        assertEquals(mockedLocalizedMessage, response.getMessage());
    }

    @Test
    public void testLocalizedMessage() {

        // given
        String messageKey = "key";
        String[] variables = new String[]{"var1", "var2"};

        String mockedMessageFromMessageSource = "messageFromMessageSource";
        given(messageSource.getMessage(messageKey, variables, Locale.ROOT)).willReturn(mockedMessageFromMessageSource);

        // when
        String result = controllerAdvice.getLocalizedMessage(messageKey, variables);

        // then
        assertEquals(mockedMessageFromMessageSource, result);
    }

    private void mockErrorResponse(ErrorKey errorKey, String message, String... variables) {
        ErrorResponse errorResponse = new ErrorResponse(errorKey, message);
        doReturn(errorResponse).when(controllerAdvice).buildErrorResponse(errorKey, variables);
    }
}

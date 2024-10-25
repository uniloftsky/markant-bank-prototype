package net.uniloftsky.markant.bank.rest;

/**
 * Response wrapper for exceptions
 */
public class ErrorResponse {

    /**
     * Exception error key
     */
    private final ErrorKey key;

    /**
     * Exception message
     */
    private final String message;

    public static ErrorResponse of(ErrorKey key, String message) {
        return new ErrorResponse(key, message);
    }

    ErrorResponse(ErrorKey key, String message) {
        this.key = key;
        this.message = message;
    }

    public ErrorKey getKey() {
        return key;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "ErrorResponse{" +
                "key=" + key +
                ", message='" + message + '\'' +
                '}';
    }

}

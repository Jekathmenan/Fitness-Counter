package ch.fhnw.fitnesscounter.exception;

import org.springframework.http.HttpStatus;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * Eigene Variation von RuntimeException, um eigene Fehlermeldungen zurückzugeben.
 *
 */
@Getter
public class FitnessAPIException extends RuntimeException {
    private final HttpStatus status;
    private final Object data;
    private Map<String, String> errorMessages = new HashMap<>();

    public FitnessAPIException (String message) {
        this(message, HttpStatus.BAD_REQUEST, null, "error");
    }

    public FitnessAPIException(String message, HttpStatus status) {
        this(message, status, null, "error");
    }

    public FitnessAPIException(HttpStatus status, Map<String, String> errorMessages) {
        this(status, null, errorMessages);
    }

    public FitnessAPIException(String message, HttpStatus status, Object data) {
        this(message, status, data, "error");
    }

    public FitnessAPIException (String message, String fieldName) {
        this(message, HttpStatus.BAD_REQUEST, null, fieldName);
    }

    public FitnessAPIException(String message, HttpStatus status, String fieldName) {
        this(message, status, null, fieldName);
    }

    public FitnessAPIException(String message, HttpStatus status, Object data, String fieldName) {
        this.status = status;
        this.data = data;
        this.errorMessages.put(fieldName, message);
    }

    public FitnessAPIException(HttpStatus status, Object data, Map<String, String> errorMessages) {
        this.status = status;
        this.data = data;
        this.errorMessages = errorMessages;
    }
}

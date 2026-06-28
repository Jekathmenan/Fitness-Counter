package ch.fhnw.fitnesscounter.exception;

import org.springframework.http.HttpStatus;
import lombok.Getter;

/**
 *
 * Eigene Variation von RuntimeException, um eigene Fehlermeldungen zurückzugeben.
 *
 */
@Getter
public class FitnessAPIException extends RuntimeException {
    private final HttpStatus status;

    public FitnessAPIException (String message) {
        this(message, HttpStatus.BAD_REQUEST);
    }

    public FitnessAPIException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }
}

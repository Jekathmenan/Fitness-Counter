package ch.fhnw.fitnesscounter.config;

import ch.fhnw.fitnesscounter.dto.error.ErrorDto;
import ch.fhnw.fitnesscounter.exception.FitnessAPIException;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * Diese Klasse ist für das Error handling der API zuständig. Sie fängt alle, von der Anwendung geworfene Fehler ab.
 * Abgefangene Fehler werden "freundlich" zurückgegeben, sodass die Frontend-Anwendung konsistente Fehlermeldungen
 * erhält.
 *
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     *
     * Diese Methode fängt allgemeine Fehler
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDto> handleAllUncaughtErrors(Exception ex) {
        // Logge den Fehler
        log.error("Unerwarteter Fehler aufgetreten: ", ex);

        // Generische Meldung zurückgeben
        Map<String, String> error = new HashMap<>();
        error.put("error", "Ein interner Serverfehler ist aufgetreten. Bitte versuchen Sie es später erneut.");

        ErrorDto errorResponse = new ErrorDto(LocalDateTime.now(), error, null);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

    /**
     *
     * Fängt ArgumentNotValidExceptions ab
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDto> handleValidationExceptions(MethodArgumentNotValidException ex) {

        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        ErrorDto errorResponse = new ErrorDto(LocalDateTime.now(), errors, null);
        return ResponseEntity.badRequest().body(errorResponse);
    }

    /**
     *
     * Fängt BadCredentialsException ab und gibt die Error-Meldung mit HTTPStatus 401 - Unauthorized zurück.
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorDto> handleBadCredential (BadCredentialsException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", ex.getMessage());

        ErrorDto errorResponse = new ErrorDto(LocalDateTime.now(), error, null);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }

    /**
     * Fängt allgemeine Sicherheitsfehler ab (z.B. Account gesperrt).
     */
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorDto> handleAuthenticationException(AuthenticationException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", "Authentifizierung fehlgeschlagen: " + ex.getMessage());

        ErrorDto errorResponse = new ErrorDto(LocalDateTime.now(), error, null);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }

    /**
     *
     * Fängt FitnessAPIException ab und gibt sie so weiter.
     * Alle anderen Exceptions unterdrückt.
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(FitnessAPIException.class)
    public ResponseEntity<ErrorDto> handleRuntime(FitnessAPIException ex) {
        ErrorDto errorResponse = new ErrorDto(LocalDateTime.now(), ex.getErrorMessages(), ex.getData());

        return ResponseEntity.status(ex.getStatus()).body(errorResponse);
    }
}
package ch.fhnw.fitnesscounter.config;

import ch.fhnw.fitnesscounter.exception.FitnessAPIException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * Diese Klasse ist für das Error handling der API zuständig. Sie fängt alle, von der Anwendung geworfene Fehler ab.
 * Abgefangene Fehler werden "freundlich" zurückgegeben, sodass die Frontend-Anwendung konsistente Fehlermeldungen
 * erhält.
 *
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     *
     * Diese Methode fängt allgemeine Fehler
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleAllUncaughtErrors(Exception ex) {
        // Logge den Fehler
        logger.error("Unerwarteter Fehler aufgetreten: ", ex);

        // Generische Meldung zurückgeben
        Map<String, String> error = new HashMap<>();
        error.put("error", "Ein interner Serverfehler ist aufgetreten. Bitte versuchen Sie es später erneut.");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }

    /**
     *
     * Fängt ArgumentNotValidExceptions ab
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return ResponseEntity.badRequest().body(errors);
    }

    /**
     *
     * Fängt BadCredentialsException ab und gibt die Error-Meldung mit HTTPStatus 401 - Unauthorized zurück.
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Map<String, String>> handleBadCredential (BadCredentialsException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
    }

    /**
     * Fängt allgemeine Sicherheitsfehler ab (z.B. Account gesperrt).
     */
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<Map<String, String>> handleAuthenticationException(AuthenticationException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", "Authentifizierung fehlgeschlagen: " + ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
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
    public ResponseEntity<Map<String, String>> handleRuntime(FitnessAPIException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", ex.getMessage());
        return ResponseEntity.status(ex.getStatus()).body(error);
    }
}
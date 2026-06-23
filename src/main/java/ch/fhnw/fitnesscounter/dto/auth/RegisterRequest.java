package ch.fhnw.fitnesscounter.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * In diesem record werden die Register-Eingaben validiert und gemappt. Wenn validation fehlschlägt, wird eine entsprechende Fehlermeldung zurückgegeben.
 *
 * @param firstName
 * @param lastName
 * @param email
 * @param password
 * @param retypePassword
 */
public record RegisterRequest(
        @NotBlank(message = "Vorname darf nicht leer sein")
        String firstName,

        @NotBlank(message = "Nachname darf nicht leer sein")
        String lastName,

        @Email(message = "Ungültige Email-Adresse")
        @NotBlank(message = "Vorname darf nicht leer sein")
        String email,

        @NotBlank(message = "Passwort darf nicht leer sein")
        @Size(min = 6, message = "Passwort muss mindestens 6 Zeichen lang sein")
        String password,

        @NotBlank(message = "Passwort darf nicht leer sein")
        String retypePassword) { }

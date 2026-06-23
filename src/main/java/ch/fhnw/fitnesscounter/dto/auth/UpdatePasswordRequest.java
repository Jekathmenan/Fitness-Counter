package ch.fhnw.fitnesscounter.dto.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdatePasswordRequest(
        @NotBlank (message = "Passwort darf nicht leer sein")
        @Size(min = 6, message = "Passwort muss mindestens 6 Zeichen lang sein")
        String newPassword,
        @NotBlank(message = "Passwort darf nicht leer sein")
        String retypePassword
) {}

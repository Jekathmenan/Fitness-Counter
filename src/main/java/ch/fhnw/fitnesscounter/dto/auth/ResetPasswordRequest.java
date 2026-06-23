package ch.fhnw.fitnesscounter.dto.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ResetPasswordRequest (
        @NotBlank (message = "Reset-Token darf nicht leer sein")
        String token,

        @NotBlank (message = "Passwort darf nicht leer sein")
        @Size (min = 6, message = "Passwort muss mindestens 6 Zeichen lang sein")
        @NotNull
        String newPassword,

        @NotNull
        @NotBlank (message = "Passwort darf nicht leer sein")
        String retypePassword) {
}

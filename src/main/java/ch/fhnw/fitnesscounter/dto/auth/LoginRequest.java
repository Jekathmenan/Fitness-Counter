package ch.fhnw.fitnesscounter.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @NotBlank(message = "Email darf nicht leer sein")
        @Email(message = "Ungültige Email-Adresse")
        String email,

        @NotBlank(message = "Passwort darf nicht leer sein")
        String password
) { }
package ch.fhnw.fitnesscounter.dto.auth;

public record UserResponse(
        String firstName,
        String lastName,
        String email,
        String role
        ) {
}

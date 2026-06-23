package ch.fhnw.fitnesscounter.dto.auth;

public record LoginResponse(String token, boolean requiresPasswordReset) {
}
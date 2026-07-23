package ch.fhnw.fitnesscounter.dto.error;

import java.time.LocalDateTime;

public record ErrorDto(
        String message,
        LocalDateTime timestamp,
        Object data
) {
}

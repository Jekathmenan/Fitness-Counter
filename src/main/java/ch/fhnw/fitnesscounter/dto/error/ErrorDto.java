package ch.fhnw.fitnesscounter.dto.error;

import java.time.LocalDateTime;
import java.util.Map;

public record ErrorDto(
        LocalDateTime timestamp,
        Map<String, String> errors,
        Object data
) {
}

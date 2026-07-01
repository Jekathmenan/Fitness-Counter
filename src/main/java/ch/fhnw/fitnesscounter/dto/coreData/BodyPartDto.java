package ch.fhnw.fitnesscounter.dto.coreData;

import jakarta.validation.constraints.*;

public record BodyPartDto(
        @Null
        Long id,
        @NotBlank(message = "Name darf nicht leer sein.")
        @NotNull
        @Pattern(regexp = "^[a-zA-ZÀ-ÿ]+$", message = "Ungültige Zeichen. Nur Buchstaben erlaubt")
        @Size(min = 2, max = 255)
        String name,
        String description
) {
}

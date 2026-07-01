package ch.fhnw.fitnesscounter.dto.coreData;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record BodyPartDto(
        @NotBlank(message = "Name darf nicht leer sein.")
        @NotNull
        @Pattern(regexp = "^[a-zA-ZÀ-ÿ]+$", message = "Ungültige Zeichen. Nur Buchstaben erlaubt")
        @Size(min = 2, max = 255)
        String name,
        String description
) {
}

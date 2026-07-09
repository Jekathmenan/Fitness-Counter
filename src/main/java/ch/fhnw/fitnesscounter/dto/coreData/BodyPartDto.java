package ch.fhnw.fitnesscounter.dto.coreData;

import ch.fhnw.fitnesscounter.model.coreData.BodyPart;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.*;

public record BodyPartDto(
        @Nullable
        Long id,
        @NotBlank(message = "Name darf nicht leer sein.")
        @NotNull
        @Pattern(regexp = "^[a-zA-ZÀ-ÿ]+$", message = "Ungültige Zeichen. Nur Buchstaben erlaubt")
        @Size(min = 2, max = 255)
        String name,
        String description
) {
        public static BodyPartDto fromEntity(BodyPart bodyPart) {
                if (bodyPart == null) return null;
                return new BodyPartDto(bodyPart.getId(), bodyPart.getName(), bodyPart.getDescription());

        }
}

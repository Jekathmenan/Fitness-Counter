package ch.fhnw.fitnesscounter.dto.coreData;

import ch.fhnw.fitnesscounter.model.coreData.BodyPart;
import ch.fhnw.fitnesscounter.model.coreData.MovementType;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Null;

public record MovementTypeDto(
        @Nullable
        Long id,

        @NotBlank(message = "Bewegungstyp muss einen Namen haben.")
        String name,

        @Nullable
        String description
) {
        public static MovementTypeDto fromEntity(MovementType movementType) {
                if (movementType == null) return null;
                return new MovementTypeDto(movementType.getId(), movementType.getName(), movementType.getDescription());
        }
}

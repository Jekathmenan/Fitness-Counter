package ch.fhnw.fitnesscounter.dto.coreData;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import java.util.List;

public record ExerciseDto(
        @Nullable
        Long id,
        @NotBlank(message = "Name darf nicht leer sein")
        String name,
        String description,

        @JsonProperty("bodyParts")
        List<BodyPartDto> bodyParts,

        @Nullable
        @JsonProperty("movements")
        List<MovementTypeDto> movements
) {
}

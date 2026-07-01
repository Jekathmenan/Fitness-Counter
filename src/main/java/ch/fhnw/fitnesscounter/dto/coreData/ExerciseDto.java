package ch.fhnw.fitnesscounter.dto.coreData;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

public record ExerciseDto (
        @NotBlank(message = "Name darf nicht leer sein")
        String name,
        String description,

        @JsonProperty("bodypartIds")
        List<Long> bodyPartIds
) {
}

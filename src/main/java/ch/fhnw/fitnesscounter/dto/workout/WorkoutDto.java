package ch.fhnw.fitnesscounter.dto.workout;

import ch.fhnw.fitnesscounter.model.auth.User;
import jakarta.annotation.Nullable;

import java.time.LocalDateTime;
import java.util.List;

public record WorkoutDto(
        @Nullable
        Long id,

        @Nullable
        String name,

        User user,

        LocalDateTime startTime,
        LocalDateTime endTime,
        List<WorkoutExerciseDto> wokoutExercises
) {
}

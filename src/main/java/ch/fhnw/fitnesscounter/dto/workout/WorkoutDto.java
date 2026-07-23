package ch.fhnw.fitnesscounter.dto.workout;

import ch.fhnw.fitnesscounter.dto.auth.UserResponse;
import ch.fhnw.fitnesscounter.model.auth.User;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Null;

import java.time.LocalDateTime;
import java.util.List;

public record WorkoutDto(
        @Nullable
        Long id,
        @Nullable
        String name,
        @Nullable
        UserResponse user, // TODO: Create a new DTO to return only necessary User data
        @Nullable
        LocalDateTime startTime,
        @Nullable
        LocalDateTime endTime,
        @Nullable
        List<WorkoutExerciseDto> workoutExercises
) {
}

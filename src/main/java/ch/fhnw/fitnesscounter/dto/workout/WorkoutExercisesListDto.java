package ch.fhnw.fitnesscounter.dto.workout;

import jakarta.annotation.Nullable;

import java.util.List;

public record WorkoutExercisesListDto(
        @Nullable
        List<WorkoutExerciseDto> workoutExercises
) {
}

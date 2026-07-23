package ch.fhnw.fitnesscounter.dto.workout;

import ch.fhnw.fitnesscounter.dto.coreData.ExerciseDto;
import ch.fhnw.fitnesscounter.model.coreData.Exercise;
import jakarta.annotation.Nullable;

import java.util.List;

public record WorkoutExerciseDto(
        @Nullable
        Long id,
        @Nullable
        List<WorkoutSetDto> workoutSets,
        ExerciseDto exercise
) {

}

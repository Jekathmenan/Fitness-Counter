package ch.fhnw.fitnesscounter.dto.workout;

import ch.fhnw.fitnesscounter.model.coreData.Exercise;

import java.util.List;

public record WorkoutExerciseDto(
        Long id,
        List<WorkoutSetDto> workoutSets,
        Exercise exercise
) {

}

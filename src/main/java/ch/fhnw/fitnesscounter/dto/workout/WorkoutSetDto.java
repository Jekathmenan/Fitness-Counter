package ch.fhnw.fitnesscounter.dto.workout;

public record WorkoutSetDto(
        Long id,
        Double weight,
        Integer reps,
        Integer setOrder
) {
}

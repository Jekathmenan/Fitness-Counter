package ch.fhnw.fitnesscounter.dto.workout;

import ch.fhnw.fitnesscounter.model.coreData.WorkoutSet;
import jakarta.annotation.Nullable;

public record WorkoutSetDto(
        @Nullable
        Long id,
        Double weight,
        Integer reps,
        @Nullable
        Integer setOrder
) {
    public WorkoutSet toEntity() {
        WorkoutSet set = new WorkoutSet();
        set.setId(id);
        set.setWeight(weight);
        set.setReps(reps);
        set.setSetOrder(setOrder);
        return set;
    }
}

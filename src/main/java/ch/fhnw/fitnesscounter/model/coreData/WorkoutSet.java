package ch.fhnw.fitnesscounter.model.coreData;

import ch.fhnw.fitnesscounter.dto.workout.WorkoutSetDto;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "workout_set")
@Getter
@Setter
public class WorkoutSet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workout_exercise_id")
    private WorkoutExercise workoutExercise;

    @NotNull(message = "Gewicht muss vorgegeben werden.")
    private Double weight;
    @NotNull(message = "Wiederholung muss vorgegeben sein und im korrekten Format sein.")
    private Integer reps;
    private Integer setOrder;

    public WorkoutSetDto toDto () {
        return new WorkoutSetDto(id, weight, reps,setOrder);
    }
}

package ch.fhnw.fitnesscounter.model.coreData;

import ch.fhnw.fitnesscounter.dto.workout.WorkoutExerciseDto;
import ch.fhnw.fitnesscounter.dto.workout.WorkoutSetDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Entity
@Table(name = "workout_exercise")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WorkoutExercise {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workout_id")
    private Workout workout;

    @ManyToOne
    @JoinColumn(name = "exercise_id")
    private Exercise exercise;

    @OneToMany(mappedBy = "workoutExercise", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("id ASC")
    private List<WorkoutSet> sets = new ArrayList<>();

    public void addSet(WorkoutSet set) {
        sets.add(set);
        set.setWorkoutExercise(this);
    }

    public Optional<WorkoutSet> findSetById (Long setId) {
        return sets.stream().filter(set -> set.getId().equals(setId)).findFirst();
    }

    public void removeSet(Long setId) {
        sets.removeIf(set -> set.getId().equals(setId));
    }

    public WorkoutExerciseDto toDto () {
        List<WorkoutSetDto> workoutSetDtos = sets.stream().map(WorkoutSet::toDto).toList();
        return new WorkoutExerciseDto(id, workoutSetDtos, exercise.toDto());
    }
}

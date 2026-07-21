package ch.fhnw.fitnesscounter.model.coreData;

import ch.fhnw.fitnesscounter.dto.workout.WorkoutExerciceDto;
import ch.fhnw.fitnesscounter.dto.workout.WorkoutSetDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "workout_exercise")
@Getter
@Setter
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
    private static List<WorkoutSet> sets = new ArrayList<>();

    public void addSet(WorkoutSet set) {
        sets.add(set);
        set.setWorkoutExercise(this);
    }

    public WorkoutExerciceDto toDto () {
        List<WorkoutSetDto> workoutSetDtos = sets.stream().map(WorkoutSet::toDto).toList();
        return new WorkoutExerciceDto(id, workoutSetDtos, exercise);
    }
}

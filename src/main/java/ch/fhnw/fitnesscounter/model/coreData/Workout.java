package ch.fhnw.fitnesscounter.model.coreData;

import ch.fhnw.fitnesscounter.dto.coreData.ExerciseDto;
import ch.fhnw.fitnesscounter.dto.workout.WorkoutDto;
import ch.fhnw.fitnesscounter.dto.workout.WorkoutExerciseDto;
import ch.fhnw.fitnesscounter.model.auth.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@Table(name = "workouts")
@NoArgsConstructor
public class Workout {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(nullable = false)
    private LocalDateTime startTime;

    private LocalDateTime endTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "workout", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<WorkoutExercise> exercises = new ArrayList<>();

    public void addExercise (WorkoutExercise exercise) {
        exercises.add(exercise);
        exercise.setWorkout(this);
    }

    public WorkoutDto toDTO()
    {
        List<ExerciseDto> exerciseDtos = exercises.stream().map(exercise -> exercise.getExercise().toDto()).toList();
        List<WorkoutExerciseDto> workoutExercises = exercises.stream().map(WorkoutExercise::toDto).toList();

        return new WorkoutDto(id, name, user, startTime, endTime, workoutExercises);
    }
}

package ch.fhnw.fitnesscounter.model.coreData;

import ch.fhnw.fitnesscounter.dto.auth.UserResponse;
import ch.fhnw.fitnesscounter.dto.coreData.ExerciseDto;
import ch.fhnw.fitnesscounter.dto.workout.WorkoutDto;
import ch.fhnw.fitnesscounter.dto.workout.WorkoutExerciseDto;
import ch.fhnw.fitnesscounter.model.auth.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@Table(name = "workouts")
@NoArgsConstructor
@Setter
@Getter
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
    @OrderBy("id ASC")
    private List<WorkoutExercise> exercises = new ArrayList<>();

    public void addExercise (WorkoutExercise exercise) {
        exercises.add(exercise);
        exercise.setWorkout(this);
    }

    public void removeExercise (Long id) {
        exercises.removeIf(exercise -> exercise.getId().equals(id));
    }

    public WorkoutDto toDTO() {
        List<WorkoutExerciseDto> workoutExercises = exercises.stream().map(WorkoutExercise::toDto).toList();
        UserResponse userResponse = new UserResponse(user.getFirstName(), user.getLastName(), user.getEmail(), user.getRole().name());
        return new WorkoutDto(id, name, userResponse, startTime, endTime, workoutExercises);
    }
}

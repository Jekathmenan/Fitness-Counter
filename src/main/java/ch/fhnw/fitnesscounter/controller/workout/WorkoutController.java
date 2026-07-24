package ch.fhnw.fitnesscounter.controller.workout;

import ch.fhnw.fitnesscounter.dto.coreData.ExerciseDto;
import ch.fhnw.fitnesscounter.dto.workout.WorkoutDto;
import ch.fhnw.fitnesscounter.dto.workout.WorkoutExerciseDto;
import ch.fhnw.fitnesscounter.dto.workout.WorkoutSetDto;
import ch.fhnw.fitnesscounter.service.workout.WorkoutService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/workout")
@RequiredArgsConstructor
public class WorkoutController {
    private final WorkoutService workoutService;

    /**
     *
     * Diese Route gibt alle Workouts des aktuellen Benutzers zurück.
     *
     * @param principal
     * @return
     */
    @GetMapping("/")
    @ResponseStatus(HttpStatus.CREATED)
    public List<WorkoutDto> getAllWorkoutsByUser (Principal principal) {
        return workoutService.getAllWorkoutsByUser(principal.getName());
    }

    /**
     *
     * Diese Route gibt alle Workouts zurück
     *
     * @return
     */
    @GetMapping("/all")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public List<WorkoutDto> getAllWorkouts () {
        return workoutService.getAllWorkouts();
    }

    /**
     *
     * Diese Route gibt das aktive Workout des aktuellen Benutzers zurück.
     *
     * @param principal
     * @return
     */
    @GetMapping("/active")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<WorkoutDto> getActiveWorkout (Principal principal) {
        WorkoutDto dto = workoutService.getActiveWorkoutForUser(principal.getName());

        if (dto == null)
            return ResponseEntity.noContent().build();

        return ResponseEntity.ok(dto);
    }

    /**
     *
     * Diese Route startet ein Training. Falls bereits ein Training gestartet wurde, wird eine Fehlermeldung gegeben.
     *
     * @param workoutDto
     * @param principal
     * @return
     */
    @PostMapping("/start")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<WorkoutDto> startWorkout (@Valid @RequestBody WorkoutDto workoutDto, Principal principal) {
        return ResponseEntity.ok(workoutService.startWorkout(workoutDto, principal.getName()));
    }

    /**
     *
     * Diese Route beendet ein Workout.
     *
     * @param id
     * @param principal
     */
    @PostMapping("/end/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void endWorkout (@PathVariable Long id, Principal principal) {
        workoutService.endWorkout(id, principal.getName());
    }

    /**
     *
     * Diese Route ist für das Hinzufügen einer Übung zu einer Route zuständig.
     *
     * @param id
     * @param exercise
     * @param principal
     * @return
     */
    @PostMapping("/{id}/exercise")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public WorkoutExerciseDto addExercise (@PathVariable Long id, @Valid @RequestBody ExerciseDto exercise, Principal principal) {
        return workoutService.addExercise(id, exercise, principal.getName());
    }
}

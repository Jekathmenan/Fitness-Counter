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

    /**
     *
     * Diese Route entfernt eine Übung aus dem aktiven Training.
     *
     * @param workoutId
     * @param exerciseId
     * @param principal
     */
    @DeleteMapping("/{workoutId}/exercise/{exerciseId}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void removeExercise (@PathVariable Long workoutId, @PathVariable Long exerciseId, Principal principal) {
        workoutService.deleteExercise(workoutId, exerciseId, principal.getName());
    }

    /**
     *
     * Diese Route fügt einen neuen Satz zur Übung hinzu
     *
     * @param workoutExerciseId
     * @param setDto
     * @param principal
     * @return
     */
    @PostMapping("/{workoutExerciseId}/set")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public WorkoutExerciseDto addSet (@PathVariable Long workoutExerciseId, @RequestBody WorkoutSetDto setDto, Principal principal) {
        return workoutService.addSet(workoutExerciseId, setDto, principal.getName());
    }

    /**
     *
     * Diese Route ersetzt ein Satz mit einem neuen Satz.
     *
     * @param workoutExerciseId
     * @param setDto
     * @param principal
     */
    @PutMapping("/{workoutExerciseId}/set")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void updateSet (@PathVariable Long workoutExerciseId, @RequestBody WorkoutSetDto setDto, Principal principal) {
        workoutService.updateSet(workoutExerciseId, setDto.id(), setDto.toEntity(), principal.getName());
    }

    /**
     *
     * Diese Route löscht eine Übung.
     *
     * @param workoutExerciseId
     * @param setId
     * @param principal
     */
    @DeleteMapping("/{workoutExerciseId}/set/{setId}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void deleteSet (@PathVariable Long workoutExerciseId, @PathVariable Long setId, Principal principal) {
        workoutService.deleteSet(workoutExerciseId, setId, principal.getName());
    }
}

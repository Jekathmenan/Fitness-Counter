package ch.fhnw.fitnesscounter.controller.workout;

import ch.fhnw.fitnesscounter.dto.workout.WorkoutDto;
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

    @GetMapping("/")
    @ResponseStatus(HttpStatus.CREATED)
    public List<WorkoutDto> getAllWorkoutsByUser (Principal principal) {
        return workoutService.getAllWorkoutsByUser(principal.getName());
    }

    @GetMapping("/all")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public List<WorkoutDto> getAllWorkouts () {
        return workoutService.getAllWorkouts();
    }

    @GetMapping("/active")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<WorkoutDto> getActiveWorkout (Principal principal) {
        WorkoutDto dto = workoutService.getActiveWorkoutForUser(principal.getName());

        if (dto == null)
            return ResponseEntity.noContent().build();

        return ResponseEntity.ok(dto);
    }

    @PostMapping("/start")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<WorkoutDto> startWorkout (@Valid @RequestBody WorkoutDto workoutDto, Principal principal) {
        return ResponseEntity.ok(workoutService.startWorkout(workoutDto, principal.getName()));
    }

    @PostMapping("/end/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void endWorkout (@PathVariable Long id, Principal principal) {
        workoutService.endWorkout(id, principal.getName());
    }

}

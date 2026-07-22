package ch.fhnw.fitnesscounter.controller.workout;

import ch.fhnw.fitnesscounter.dto.workout.WorkoutDto;
import ch.fhnw.fitnesscounter.service.workout.WorkoutService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
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

    @GetMapping("/get-all")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public List<WorkoutDto> getAllWorkouts () {
        return workoutService.getAllWorkouts();
    }

    @PostMapping("/")
    @ResponseStatus(HttpStatus.CREATED)
    public void createWorkout (@Valid @RequestBody WorkoutDto workoutDto) {

    }

}

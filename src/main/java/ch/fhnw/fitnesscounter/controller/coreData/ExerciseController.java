package ch.fhnw.fitnesscounter.controller.coreData;

import ch.fhnw.fitnesscounter.dto.coreData.ExerciseDto;
import ch.fhnw.fitnesscounter.dto.coreData.ExercisesListDto;
import ch.fhnw.fitnesscounter.service.coreData.ExerciseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/exercise")
@RequiredArgsConstructor
public class ExerciseController {
    private final ExerciseService exerciseService;

    @PostMapping("/")
    @ResponseStatus(HttpStatus.CREATED)
    public void createExercise (@Valid @RequestBody ExerciseDto exerciseDto, Principal principal) {
        exerciseService.createExercise(exerciseDto, principal.getName());
    }

    @PostMapping("/add-many")
    @ResponseStatus(HttpStatus.CREATED)
    public void createMultipleExercises(@Valid @RequestBody ExercisesListDto exerciseList, Principal principal) {
        exerciseService.createMultipleExercises(exerciseList, principal.getName());
    }
}

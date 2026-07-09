package ch.fhnw.fitnesscounter.controller.coreData;

import ch.fhnw.fitnesscounter.dto.coreData.ExerciseDto;
import ch.fhnw.fitnesscounter.dto.coreData.ExerciseGetDto;
import ch.fhnw.fitnesscounter.dto.coreData.ExercisesListDto;
import ch.fhnw.fitnesscounter.service.coreData.ExerciseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/exercise")
@RequiredArgsConstructor
public class ExerciseController {
    private final ExerciseService exerciseService;

    /**
     *
     * Mit dieser Route lassen sich alle Exercises auslesen.
     *
     * @param principal
     * @return
     */
    @GetMapping("/")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public List<ExerciseDto> getExercises (Principal principal) {
        return exerciseService.getAllExercises();
    }

    /**
     *
     * Diese Route erlaubt die Suche nach einer bestimmten Exercise.
     *
     * @param id
     * @param principal
     * @return
     */
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ExerciseDto getExerciseById (@PathVariable Long id, Principal principal) {
        return exerciseService.getExerciseById(id, principal.getName());
    }

    @PostMapping("/")
    @ResponseStatus(HttpStatus.CREATED)
    public void createExercise (@Valid @RequestBody ExerciseDto exerciseDto, Principal principal) {
        exerciseService.createExercise(exerciseDto, principal.getName());
    }

    /**
     *
     * Diese Route erlaubt die Erfassung von mehreren Exercices.
     *
     * @param exerciseList
     * @param principal
     */
    @PostMapping("/add-many")
    @ResponseStatus(HttpStatus.CREATED)
    public void createMultipleExercises(@Valid @RequestBody ExercisesListDto exerciseList, Principal principal) {
        exerciseService.createMultipleExercises(exerciseList, principal.getName());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void deleteExercise (@PathVariable Long id, Principal principal) {
        exerciseService.deleteExercise(id, principal.getName());
    }
}

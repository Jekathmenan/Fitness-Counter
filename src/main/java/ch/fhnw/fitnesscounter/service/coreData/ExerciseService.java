package ch.fhnw.fitnesscounter.service.coreData;

import ch.fhnw.fitnesscounter.dto.coreData.ExerciseDto;
import ch.fhnw.fitnesscounter.dto.coreData.ExerciseGetDto;
import ch.fhnw.fitnesscounter.dto.coreData.ExercisesListDto;
import ch.fhnw.fitnesscounter.exception.FitnessAPIException;
import ch.fhnw.fitnesscounter.model.coreData.BodyPart;
import ch.fhnw.fitnesscounter.model.coreData.Exercise;
import ch.fhnw.fitnesscounter.repository.BodyPartsRepository;
import ch.fhnw.fitnesscounter.repository.ExerciseRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.HashSet;
import java.util.List;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class ExerciseService {
    private final ExerciseRepository exerciseRepository;
    private final BodyPartsRepository bodyPartsRepository;

    /**
     *
     * Diese Methode gibt alle Exercises zurück.
     *
     * @return
     */
    public List<ExerciseGetDto> getAllExercises () {
        return exerciseRepository.findAll().stream()
                .map(ex -> new ExerciseGetDto(
                        ex.getName(),
                        ex.getDescription(),
                        ex.getTrainedBodyPartsAsDto()
                ))
                .toList();
    }

    /**
     *
     * Diese Methode gibt, wenn gefunden, die Exercise als DTO zurück.
     *
     * @param id
     * @param user
     * @return
     */
    public ExerciseGetDto getExerciseById (Long id, String user) {
        return exerciseRepository.findById(id).orElseThrow(() -> {
            log.warn("User {} tried to get a Exercise that does not exist: {}.", user, id);
            return new FitnessAPIException("Exercise does not exist", HttpStatus.NOT_FOUND);
        }).toDto();
    }

    /**
     *
     * Diese Methode speichert eine neu Exercise in der Datenbank.
     *
     * @param dto
     * @param email
     */
    public void createExercise (ExerciseDto dto, String email) {
        // Erstelle ein neues Exercise Objekt
        Exercise exercise = Exercise.builder()
                .name(dto.name())
                .description(dto.description())
                .build();

        // Lese die Ids aller mitgegebenen BodyParts
        List<BodyPart> bodyParts = bodyPartsRepository.findAllById(dto.bodyPartIds());

        if (bodyParts.size() != dto.bodyPartIds().size()) {
            log.info("Einige der angegebenen BodyParts für das Erstellen der Exercise {} wurden nicht gefunden. Exercise nicht angelegt.", exercise.getName());
            throw new FitnessAPIException("Einige der BodyParts nicht gefunden. Exercise nicht angelegt.", HttpStatus.NOT_FOUND);
        }

        // Weise BodyParts dem exercise zu und speichere es
        exercise.setTrainedBodyParts(new HashSet<>(bodyParts));
        exerciseRepository.save(exercise);
    }

    /**
     *
     * Diese Methode erstellt mehrere angegebene Übungen → Ruft createExercise auf.
     *
     * @param exercisesList
     * @param user
     */
    public void createMultipleExercises (ExercisesListDto exercisesList, String user) {
        exercisesList.getExercises().forEach(e -> createExercise(e, user));
    }
}

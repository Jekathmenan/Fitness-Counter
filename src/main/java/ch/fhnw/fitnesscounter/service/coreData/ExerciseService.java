package ch.fhnw.fitnesscounter.service.coreData;

import ch.fhnw.fitnesscounter.dto.coreData.ExerciseDto;
import ch.fhnw.fitnesscounter.dto.coreData.ExercisesListDto;
import ch.fhnw.fitnesscounter.exception.FitnessAPIException;
import ch.fhnw.fitnesscounter.model.coreData.BodyPart;
import ch.fhnw.fitnesscounter.model.coreData.Exercise;
import ch.fhnw.fitnesscounter.repository.BodyPartsRepository;
import ch.fhnw.fitnesscounter.repository.ExerciseRepository;
import ch.fhnw.fitnesscounter.util.DataNormalizer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
        if (exerciseRepository.existsByName(dto.name()))
            return;

        // Erstelle ein neues Exercise Objekt
        Exercise exercise = Exercise.builder()
                .name(dto.name())
                .description(dto.description())
                .build();

        // Lese alle BodyParts aus
        Set<BodyPart> bodyParts = dto.bodyParts().stream().map(this::findOrCreateBodyPart) // Verhindere Doppelte BodyParts mit Set
                .collect(Collectors.toSet());

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
    /**
     *
     * Diese Hilfsmethode findet den BodyPart nach dem Namen. Falls nicht gefunden wird die BodyPart neu erstellt.
     *
     * @param bpDto
     * @return
     */
    private BodyPart findOrCreateBodyPart(BodyPartDto bpDto) {
        return bodyPartsRepository.findByNameIgnoreCase(DataNormalizer.normalizeString(bpDto.name()))
                .orElseGet(() -> {
                    BodyPart newBp = new BodyPart();
                    newBp.setName(DataNormalizer.normalizeString(bpDto.name()));
                    newBp.setDescription(bpDto.description());
                    return bodyPartsRepository.save(newBp);
                });
    }
}

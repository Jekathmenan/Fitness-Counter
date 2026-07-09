package ch.fhnw.fitnesscounter.service.coreData;

import ch.fhnw.fitnesscounter.dto.coreData.BodyPartDto;
import ch.fhnw.fitnesscounter.dto.coreData.ExerciseDto;
import ch.fhnw.fitnesscounter.dto.coreData.ExercisesListDto;
import ch.fhnw.fitnesscounter.dto.coreData.MovementTypeDto;
import ch.fhnw.fitnesscounter.exception.FitnessAPIException;
import ch.fhnw.fitnesscounter.model.coreData.BodyPart;
import ch.fhnw.fitnesscounter.model.coreData.Exercise;
import ch.fhnw.fitnesscounter.model.coreData.MovementType;
import ch.fhnw.fitnesscounter.repository.BodyPartsRepository;
import ch.fhnw.fitnesscounter.repository.ExerciseRepository;
import ch.fhnw.fitnesscounter.repository.MovementTypeRepository;
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
    private final MovementTypeRepository movementTypeRepository;

    /**
     *
     * Diese Methode gibt alle Exercises zurück.
     *
     * @return
     */
    public List<ExerciseDto> getAllExercises () {
        return exerciseRepository.findAll().stream()
                .map(ex -> new ExerciseDto(
                        ex.getId(),
                        ex.getName(),
                        ex.getDescription(),
                        ex.getTrainedBodyPartsAsDto(),
                        ex.getMovementTypesAsDto()
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
    public ExerciseDto getExerciseById (Long id, String user) {
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

        // Lese alle Bewegungstypen aus oder erstelle sie und weise sie der Übung zu.
        Set<MovementType> movementTypes = dto.movements().stream().map(this::findOrCreateMovementType).collect(Collectors.toSet());
        exercise.setMovementTypes(movementTypes);

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
     * Diese Methode aktualisiert eine bestehende Übung mit neuen Daten.
     *
     * @param id
     * @param exerciseDto
     * @param user
     */
    public  void updateExercise (Long id, ExerciseDto exerciseDto, String user) {
        Exercise exercise = exerciseRepository.findById(id).orElseThrow(() -> {
            log.warn("User {} tried to update a non existing Exercise {}", user, id);
            return new FitnessAPIException("Exercise by id " + id + " does not exist.", HttpStatus.NOT_FOUND);
        });

        Set<BodyPart> bodyParts = exerciseDto.bodyParts().stream().map(this::findOrCreateBodyPart).collect(Collectors.toSet());
        Set<MovementType> movementTypes = exerciseDto.movements().stream().map(this::findOrCreateMovementType).collect(Collectors.toSet());

        exercise.setName(exerciseDto.name());
        exercise.setDescription(exerciseDto.description());
        exercise.setTrainedBodyParts(bodyParts);
        exercise.setMovementTypes(movementTypes);
        exerciseRepository.save(exercise);
    }

    /**
     *
     * Diese Methode löscht eine Übung.
     *
     * @param id
     * @param user
     */
    public void deleteExercise (Long id, String user) {
        if (!exerciseRepository.existsById(id)){
            log.warn("User {} tried to delete a non existing Exercise {}", user, id);
            return;
        }

        exerciseRepository.deleteById(id);
        log.info("User {} successfully deleted exercise {}", user, id);
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

    private MovementType findOrCreateMovementType (MovementTypeDto dto) {
        return movementTypeRepository.findByNameIgnoreCase(dto.name())
                .orElseGet(() -> {
                    MovementType movementType = new MovementType();
                    movementType.setName(dto.name());
                    movementType.setDescription(dto.description());
                    return movementTypeRepository.save(movementType);
                }
        );
    }
}

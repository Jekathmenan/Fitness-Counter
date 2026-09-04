package ch.fhnw.fitnesscounter.service.workout;

import ch.fhnw.fitnesscounter.dto.coreData.ExerciseDto;
import ch.fhnw.fitnesscounter.dto.workout.*;
import ch.fhnw.fitnesscounter.exception.FitnessAPIException;
import ch.fhnw.fitnesscounter.model.auth.User;
import ch.fhnw.fitnesscounter.model.coreData.Exercise;
import ch.fhnw.fitnesscounter.model.coreData.Workout;
import ch.fhnw.fitnesscounter.model.coreData.WorkoutExercise;
import ch.fhnw.fitnesscounter.model.coreData.WorkoutSet;
import ch.fhnw.fitnesscounter.repository.ExerciseRepository;
import ch.fhnw.fitnesscounter.repository.UserRepository;
import ch.fhnw.fitnesscounter.repository.WorkoutExerciseRepository;
import ch.fhnw.fitnesscounter.repository.WorkoutRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class WorkoutService {
    private final UserRepository userRepository;
    private final WorkoutRepository workoutRepository;
    private final ExerciseRepository exerciseRepository;
    private final WorkoutExerciseRepository workoutExerciseRepository;

    /**
     *
     * Diese Methode gibt alle Workouts als DTOs zurück
     *
     * @return
     */
    public List<WorkoutDto> getAllWorkouts () {
        return workoutRepository.findAll().stream().map(Workout::toDTO).toList();
    }

    /**
     *
     * Diese Methode gibt alle Workouts des aktuell eingeloggten Benutzers als DTOs zurück.
     *
     * @param email
     * @return
     */
    public List<WorkoutDto> getAllWorkoutsByUser(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new FitnessAPIException("Unknown Error occurred. Please contact a Server Admin.", HttpStatus.NOT_FOUND));
        return workoutRepository.findByUserId(user.getId()).stream().map(Workout::toDTO).toList();
    }

    /**
     *
     * Diese Methode gibt das aktive Workout des eingeloggten Users zurück.
     *
     * @param email
     * @return
     */
    public WorkoutDto getActiveWorkoutForUser (String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new FitnessAPIException("Unknown Error occurred. Please contact a Server Admin.", HttpStatus.NOT_FOUND));
        return workoutRepository.findFirstByUserIdAndEndTimeIsNull(user.getId())
                .map(Workout::toDTO)
                .orElse(null);
    }

    /**
     *
     * Diese Methode gibt alle Übungen des aktiven Trainings zurück.
     *
     * @param email
     * @return
     */
    public WorkoutExercisesListDto getActiveExercisesForUser(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new FitnessAPIException("User with email " + email + " not found.", HttpStatus.NOT_FOUND));

        // Prüfe, ob User ein aktives Training hat
        Workout workout = workoutRepository.findFirstByUserIdAndEndTimeIsNull(user.getId())
                .orElse(null);
        if (workout == null)
            return new WorkoutExercisesListDto(List.of());

        // Gebe alle Übungen des aktiven Trainings zurück
        List<WorkoutExerciseDto> exercises = workout.getExercises().stream()
                .map(WorkoutExercise::toDto)
                .toList();

        return new WorkoutExercisesListDto(exercises);
    }

    /**
     *
     * Diese Methode gibt alle Sätze einer Übung zurück
     *
     * @param email
     * @param id
     * @return
     */
    public WorkoutSetListDto getExercisesSets (String email, Long id) {
        // Finde den user
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new FitnessAPIException("User with email " + email + " not found.", HttpStatus.NOT_FOUND));

        // Lese die Übung mit der angegebenen Id aus.
        WorkoutExercise exercise = workoutRepository.findFirstByUserIdAndEndTimeIsNull(user.getId()).orElseThrow(()-> new FitnessAPIException("Keine aktives Training am laufen!"))
                .getExercises().stream().filter(ex -> ex.getId().equals(id))
                // Wenn Übung nicht gefunden, Exception werfen
                .findFirst().orElseThrow(() -> new FitnessAPIException("Übung mit der angegebenen Id nicht gefunden!"));

        // Lese alle Sätze der ausgewählten Übung aus und gebe sie zurück
        List<WorkoutSetDto> workoutSetListDto = exercise.getSets().stream().map(WorkoutSet::toDto).toList();
        return new WorkoutSetListDto(workoutSetListDto);
    }

    /**
     *
     * Diese Methode startet einen neuen Workout. Falls der Benutzer bereits ein Workout gestartet hat, wird eine Fehlermeldung geworfen, dass bereits ein Workout gestartet ist.
     *
     * @param workoutDto
     * @param email
     * @return
     */
    public WorkoutDto startWorkout (WorkoutDto workoutDto, String email) {
        User user = userRepository.findByEmail(email).orElseThrow(()-> new FitnessAPIException("Unknown Error occurred. Please contact a Server Admin.", HttpStatus.NOT_FOUND));

        // Prüfe, ob der Benutzer bereits ein Workout gestartet hat.
        Optional<Workout> activeWorkout = workoutRepository.findFirstByUserIdAndEndTimeIsNull(user.getId());

        if(activeWorkout.isPresent())
            throw new FitnessAPIException("Es darf nur ein Training pro Benutzer gestartet werden.", HttpStatus.CONFLICT, activeWorkout.get());

        Workout workout = new Workout();
        workout.setName(workoutDto.name());
        workout.setStartTime(LocalDateTime.now());
        workout.setUser(user);

        workoutRepository.save(workout);
        return workout.toDTO();
    }

    /**
     *
     * Diese Methode beendet ein bereits gestartetes Workout.
     *
     * @param id
     * @param email
     */
    public void endWorkout(Long id, String email) {
        // Suche das vom Benutzer angegebene Workout
        Workout w = workoutRepository.findByIdAndEndTimeIsNull(id).orElseThrow(() -> {
            log.warn("User {} tried to stop a non existing workout {}.", email, id);
            return  new FitnessAPIException("Workout existiert nicht!", HttpStatus.NOT_FOUND);
        });

        // Prüfe, ob das Workout tatsächlich dem eingeloggten Benutzer gehört
        if (!w.getUser().getEmail().equals(email))
            throw new FitnessAPIException("Keine Berechtigung für dieses Training", HttpStatus.FORBIDDEN);

        // Beende das Workout
        w.setEndTime(LocalDateTime.now());
        workoutRepository.save(w);
    }

    /**
     *
     * Diese Methode erlaubt as einfügen einer neuen TrainingsÜbung.
     *
     * @param id
     * @param exerciseDto
     * @param email
     * @return
     */
    public WorkoutExerciseDto addExercise (Long id, ExerciseDto exerciseDto, String email) {
        // Suche das passende Workout
        Workout workout = findWorkoutOrThrow(id, email);

        // Prüfe, ob der Name der Übung leer ist und ob die Übung existiert
        if (exerciseDto.name() == null || exerciseDto.name().isBlank())
            throw new FitnessAPIException("Übungsname darf nicht leer sein.", HttpStatus.BAD_REQUEST);

        // TODO: Neue Übung erstellen, wenn sie nicht existiert.
        Exercise exercise = exerciseRepository.findByName(exerciseDto.name()).
                orElseThrow(() -> new FitnessAPIException("Übung existiert nicht.", HttpStatus.EXPECTATION_FAILED));

        // Speichere die Trainingsübung
        WorkoutExercise workoutExercise = new WorkoutExercise();
        workoutExercise.setExercise(exercise);

        workoutExerciseRepository.save(workoutExercise);
        workout.addExercise(workoutExercise);
        workoutRepository.save(workout);
        return workoutExercise.toDto();
    }

    /**
     *
     * Diese Methode löscht eine Übung aus einer Trainingseinheit.
     *
     * @param workoutId
     * @param exerciseId
     * @param email
     */
    public void deleteExercise (Long workoutId, Long exerciseId, String email) {
        Workout workout = findWorkoutOrThrow(workoutId , email);
        workout.removeExercise(exerciseId);
        workoutRepository.save(workout);
    }

    /**
     *
     * Diese Methode erlaubt dem Benutzer einen neuen Satz in die Übung eines laufenden Workouts einzufügen
     *
     * @param workoutExerciseId
     * @param workoutSetDto
     * @param email
     * @return
     */
    public WorkoutExerciseDto addSet (Long workoutExerciseId, WorkoutSetDto workoutSetDto, String email) {
        WorkoutExercise exercise = findWorkoutExerciseOrThrow(workoutExerciseId, email);

        if (exercise.getWorkout().getEndTime() != null)
            throw new FitnessAPIException("Workout ist bereits beendet. Es dürfen keine weiteren Sätze hinzugefügt werden.", HttpStatus.FORBIDDEN);

        WorkoutSet set = workoutSetDto.toEntity();

        exercise.addSet(set);
        workoutExerciseRepository.save(exercise);
        return exercise.toDto();
    }

    public void updateSet (Long workoutExerciseId, Long setId, WorkoutSet newSet, String email) {
        WorkoutExercise exercise = findWorkoutExerciseOrThrow(workoutExerciseId, email);


        if (exercise.getWorkout().getEndTime() != null)
            throw new FitnessAPIException("Workout ist bereits beendet. Es dürfen keine Sätze verändert werden.", HttpStatus.FORBIDDEN);
        WorkoutSet workoutSet = exercise.findSetById(setId)
                .orElseThrow(() -> new FitnessAPIException("Satz nicht gefunden", HttpStatus.NOT_FOUND));

        workoutSet.setReps(newSet.getReps());
        workoutSet.setWeight(newSet.getWeight());

        workoutExerciseRepository.save(exercise);

    }

    /**
     *
     * Diese Methode löscht den Satz aus einer Übung
     *
     * @param exerciseId
     * @param setId
     * @param email
     */
    public void deleteSet (Long exerciseId, Long setId, String email) {
        WorkoutExercise exercise = findWorkoutExerciseOrThrow(exerciseId, email);
        if (exercise.getWorkout().getEndTime() != null)
            throw new FitnessAPIException("Workout ist bereits beendet. Es dürfen keine Sätze gelöscht werden.", HttpStatus.FORBIDDEN);

        exercise.removeSet(setId);
        workoutExerciseRepository.save(exercise);
    }

    /**
     *
     * Dieser Hilfsmethode sucht die Übung. Falls die Übung nicht dem eingeloggten Benutzer gehört wirft sie eine Ausnahme.
     *
     * @param id
     * @param email
     * @return
     */
    private Workout findWorkoutOrThrow(Long id, String email) {
        Workout workout = workoutRepository.findByIdAndEndTimeIsNull(id).orElseThrow(() -> new FitnessAPIException("", HttpStatus.NOT_FOUND));

        // Prüfe, ob das Workout tatsächlich dem eingeloggten Benutzer gehört
        if (!workout.getUser().getEmail().equals(email))
            throw new FitnessAPIException("Keine Berechtigung für dieses Training", HttpStatus.FORBIDDEN);

        return workout;
    }

    /**
     *
     * Diese Hilfsmethode findet die passende Trainingsübung und prüft, ob sie dem eingeloggten Benutzer gehört.
     *
     * @param workoutExerciseId
     * @param email
     * @return
     */
    private WorkoutExercise findWorkoutExerciseOrThrow (Long workoutExerciseId, String email) {
        WorkoutExercise exercise = workoutExerciseRepository.findById(workoutExerciseId)
                .orElseThrow(() -> new FitnessAPIException("Angegebene Übung nicht gefunden.", HttpStatus.NOT_FOUND));

        // Prüfe, ob die Trainingsübung dem eingeloggten User gehört
        User workoutUser = exercise.getWorkout().getUser();
        if (!workoutUser.getEmail().equals(email))
            throw new FitnessAPIException("Keine Berechtigung für dieses Training", HttpStatus.FORBIDDEN);

        return exercise;
    }
}

package ch.fhnw.fitnesscounter.service.workout;

import ch.fhnw.fitnesscounter.dto.coreData.ExerciseDto;
import ch.fhnw.fitnesscounter.dto.workout.WorkoutDto;
import ch.fhnw.fitnesscounter.dto.workout.WorkoutExerciseDto;
import ch.fhnw.fitnesscounter.exception.FitnessAPIException;
import ch.fhnw.fitnesscounter.model.auth.User;
import ch.fhnw.fitnesscounter.model.coreData.Exercise;
import ch.fhnw.fitnesscounter.model.coreData.Workout;
import ch.fhnw.fitnesscounter.model.coreData.WorkoutExercise;
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

    public WorkoutExerciseDto addExercise (Long id, ExerciseDto exerciseDto, String email) {
        // Suche das passende Workout
        Workout workout = workoutRepository.findByIdAndEndTimeIsNull(id).orElseThrow(() -> new FitnessAPIException("", HttpStatus.NOT_FOUND));

        // Prüfe, ob das Workout tatsächlich dem eingeloggten Benutzer gehört
        if (!workout.getUser().getEmail().equals(email))
            throw new FitnessAPIException("Keine Berechtigung für dieses Training", HttpStatus.FORBIDDEN);

        //
        if (exerciseDto.name() == null || exerciseDto.name().isBlank())
            throw new FitnessAPIException("Übungsname darf nicht leer sein.", HttpStatus.BAD_REQUEST);

        Exercise exercise = exerciseRepository.findByName(exerciseDto.name()).
                orElseThrow(() -> new FitnessAPIException("Übung existiert nicht.", HttpStatus.EXPECTATION_FAILED));

        WorkoutExercise workoutExercise = new WorkoutExercise();
        workoutExercise.setExercise(exercise);

        workoutExerciseRepository.save(workoutExercise);
        workout.addExercise(workoutExercise);
        workoutRepository.save(workout);
        return workoutExercise.toDto();
    }
}

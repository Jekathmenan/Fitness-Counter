package ch.fhnw.fitnesscounter.service.workout;

import ch.fhnw.fitnesscounter.dto.workout.WorkoutDto;
import ch.fhnw.fitnesscounter.exception.FitnessAPIException;
import ch.fhnw.fitnesscounter.model.auth.User;
import ch.fhnw.fitnesscounter.model.coreData.Workout;
import ch.fhnw.fitnesscounter.repository.UserRepository;
import ch.fhnw.fitnesscounter.repository.WorkoutRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class WorkoutService {
    private final UserRepository userRepository;
    private final WorkoutRepository workoutRepository;

    public List<WorkoutDto> getAllWorkouts () {
        return workoutRepository.findAll().stream().map(Workout::toDTO).toList();
    }

    public List<WorkoutDto> getAllWorkoutsByUser(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new FitnessAPIException("Unknown Error occurred. Please contact a Server Admin.", HttpStatus.NOT_FOUND));
        return workoutRepository.findByUserId(user.getId()).stream().map(Workout::toDTO).toList();
    }
}

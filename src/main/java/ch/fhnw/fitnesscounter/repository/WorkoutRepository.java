package ch.fhnw.fitnesscounter.repository;

import ch.fhnw.fitnesscounter.model.coreData.Workout;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WorkoutRepository extends JpaRepository<Workout, Long>  {
    List<Workout> findByUserId(Long userId);
    List<Workout> findByUserIdAndEndTimeIsNotNull(Long userId);
}

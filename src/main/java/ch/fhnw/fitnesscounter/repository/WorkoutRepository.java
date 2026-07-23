package ch.fhnw.fitnesscounter.repository;

import ch.fhnw.fitnesscounter.model.coreData.Workout;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface WorkoutRepository extends JpaRepository<Workout, Long>  {
    List<Workout> findByUserId(Long userId);
    Optional<Workout> findByIdAndEndTimeIsNull(Long userId);
    Optional<Workout> findFirstByUserIdAndEndTimeIsNull(Long userId);
}

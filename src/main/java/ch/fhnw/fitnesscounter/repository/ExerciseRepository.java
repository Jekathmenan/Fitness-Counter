package ch.fhnw.fitnesscounter.repository;

import ch.fhnw.fitnesscounter.model.coreData.Exercise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface ExerciseRepository extends JpaRepository<Exercise, Long> {
    boolean existsByName(String name);
    Optional<Exercise> findByName(String name);
    List<Exercise> findAllByOrderByIdDesc();
}

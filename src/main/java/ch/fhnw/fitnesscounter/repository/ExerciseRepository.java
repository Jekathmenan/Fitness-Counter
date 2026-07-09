package ch.fhnw.fitnesscounter.repository;

import ch.fhnw.fitnesscounter.model.coreData.Exercise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExerciseRepository extends JpaRepository<Exercise, Long> {
    boolean existsByName(String name);
}

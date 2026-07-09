package ch.fhnw.fitnesscounter.repository;

import ch.fhnw.fitnesscounter.model.coreData.BodyPart;
import ch.fhnw.fitnesscounter.model.coreData.MovementType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MovementTypeRepository extends JpaRepository<MovementType, Long> {
    boolean existsByName(String name);
    Optional<MovementType> findByNameIgnoreCase(String name);
}

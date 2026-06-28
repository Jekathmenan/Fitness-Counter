package ch.fhnw.fitnesscounter.repository;

import ch.fhnw.fitnesscounter.model.bodypart.BodyPart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BodyPartsRepository extends JpaRepository<BodyPart, Long> {
    boolean existsByNameIgnoreCase(String name);
}

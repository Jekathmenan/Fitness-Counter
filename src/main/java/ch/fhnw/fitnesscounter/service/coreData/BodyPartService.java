package ch.fhnw.fitnesscounter.service.coreData;

import ch.fhnw.fitnesscounter.dto.coreData.BodyPartDto;
import ch.fhnw.fitnesscounter.exception.FitnessAPIException;
import ch.fhnw.fitnesscounter.model.coreData.BodyPart;
import ch.fhnw.fitnesscounter.repository.BodyPartsRepository;
import ch.fhnw.fitnesscounter.util.DataNormalizer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class BodyPartService {
    private final BodyPartsRepository bodyPartsRepository;

    /**
     *
     * Diese Methode liest alle erfassten BodyParts aus und gibt sie zurück als DTOs
     *
     * @return
     */
    public List<BodyPartDto> getAllBodyParts () {
        return bodyPartsRepository.findAllByOrderByIdDesc().stream()
                // mappe die gefundenen BodyParts zu BodyPartDtos
                .map(bp -> new BodyPartDto(
                        bp.getId(),
                        bp.getName(),
                        bp.getDescription(),
                        bp.getExercises().isEmpty()
                )).toList();
    }

    /**
     *
     * Diese Methode gibt, wenn gefunden, die BodyPart als DTO zurück.
     *
     * @param id
     * @param user
     * @return
     */
    public BodyPartDto getBodyPartById (Long id, String user) {
        return bodyPartsRepository.findById(id).orElseThrow(() -> {
            log.warn("User {} tried to read a BodyPart that does not exist {}.", user, id);
            return new FitnessAPIException("BodyPart does not exist", HttpStatus.NOT_FOUND);
        }).toDto();
    }

    /**
     *
     * Adds a given Body part/Muscle to db, if it does not exist. For now any User can create his Body Parts
     *
     * @param bodyPartDto
     */
    public void createBodyPart(BodyPartDto bodyPartDto, String user) {
        if (bodyPartsRepository.existsByNameIgnoreCase(bodyPartDto.name()))
            return;

        BodyPart bodyPart = BodyPart.builder()
                .name(DataNormalizer.normalizeString(bodyPartDto.name()))
                .description(bodyPartDto.description())
                .build();

        bodyPartsRepository.save(bodyPart);
        log.info("Neuer Körperteil von {} angelegt {}", user, bodyPartDto.name());
    }

    /**
     *
     * Diese Methode erlaubt die Erfassung von mehreren Körperteilen.
     *
     * @param dtos
     * @param user
     */
    public void createMany (List<BodyPartDto> dtos, String user) {
        dtos.forEach(dto -> createBodyPart(dto, user));
    }

    /**
     *
     * Diese Methode ist für das Ändern eines bestehenden Körperteils zuständig.
     *
     * @param id
     * @param dto
     * @param user
     */
    public void updateBodyPart(long id, BodyPartDto dto, String user) {
        bodyPartsRepository.findById(id).ifPresentOrElse(bodyPart1 -> {
            bodyPart1.updateFrom(dto);
            bodyPartsRepository.save(bodyPart1);
        },
        () -> {
            log.warn("User {} tried to update a non existing body part {}.", user, id);
            throw new FitnessAPIException("Body part not found.", HttpStatus.NOT_FOUND);
        });
    }

    /**
     *
     * Diese Methode ist für das Löschen eines Körperteils zuständig.
     *
     * @param id
     * @param user
     */
    public void deleteBodyPart (long id, String user) {
        BodyPart bodyPart = bodyPartsRepository.findById(id).orElseThrow(() -> {
            log.warn("User {} tried to delete a non existing body part {}.", user, id);
            return new FitnessAPIException("Body part not found.", HttpStatus.NOT_FOUND);
        });

        // Körperteil nur löschen, wenn es nicht verwendet wird.
        if (bodyPart.getExercises().isEmpty()) {
            bodyPartsRepository.delete(bodyPart);
        } else {
            throw new FitnessAPIException("Cannot delete body part it is already being used.", HttpStatus.FORBIDDEN);
        }
    }
}

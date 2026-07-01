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
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class BodyPartService {
    private final BodyPartsRepository bodyPartsRepository;

    public List<BodyPartDto> getAllBodyParts () {
        List<BodyPartDto> bodyParts = new ArrayList<>();
        bodyPartsRepository.findAll().forEach(bp -> {
            BodyPartDto dto = new BodyPartDto(bp.getId(), bp.getName(), bp.getDescription());
            bodyParts.add(dto);
        });
        return bodyParts;
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

        bodyPartsRepository.findById(id).ifPresentOrElse(
                bodyPartsRepository::delete,
                () -> {
                    log.warn("User {} tried to delete a non existing body part {}.", user, id);
                    throw new FitnessAPIException("Body part not found.", HttpStatus.NOT_FOUND);
                }
        );
    }
}

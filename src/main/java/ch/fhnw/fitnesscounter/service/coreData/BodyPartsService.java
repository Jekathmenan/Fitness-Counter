package ch.fhnw.fitnesscounter.service.coreData;

import ch.fhnw.fitnesscounter.dto.core.BodyPartDto;
import ch.fhnw.fitnesscounter.model.bodypart.BodyPart;
import ch.fhnw.fitnesscounter.repository.BodyPartsRepository;
import ch.fhnw.fitnesscounter.util.DataNormalizer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class BodyPartsService {
    private final BodyPartsRepository bodyPartsRepository;

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
}

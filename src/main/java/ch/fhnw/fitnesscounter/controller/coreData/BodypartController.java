package ch.fhnw.fitnesscounter.controller.coreData;

import ch.fhnw.fitnesscounter.dto.coreData.BodyPartDto;
import ch.fhnw.fitnesscounter.dto.coreData.BodyPartsListDto;
import ch.fhnw.fitnesscounter.model.coreData.BodyPart;
import ch.fhnw.fitnesscounter.repository.BodyPartsRepository;
import ch.fhnw.fitnesscounter.service.coreData.BodyPartsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/body-part")
@RequiredArgsConstructor
public class BodypartController {
    private final BodyPartsRepository bodyPartsRepository;
    private final BodyPartsService bodyPartsService;

    /**
     *
     * Diese Methode gibt alle Körperteile/Muskeln zurück.
     *
     * @return
     */
    @GetMapping("/")
    public List<BodyPart> getBodyParts () {
        return bodyPartsRepository.findAll();
    }

    /**
     *
     * Erlaubt die Erfassung von neuen Körperteilen/Muskeln.
     *
     * @param bodyPart
     */
    @PostMapping("/")
    @ResponseStatus(HttpStatus.CREATED)
    public void createBodyPart(@Valid @RequestBody BodyPartDto bodyPart, Principal principal) {
        bodyPartsService.createBodyPart(bodyPart, principal.getName());
    }

    @PostMapping("/add-many")
    @ResponseStatus(HttpStatus.CREATED)
    public void createMultipleBodyParts(@Valid @RequestBody BodyPartsListDto bodyPartsList, Principal principal) {
        bodyPartsService.createMany(bodyPartsList.getBodyParts(), principal.getName());
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void updateBodyPart(@PathVariable Long id, @Valid @RequestBody BodyPartDto bodyPartDto, Principal principal) {
        bodyPartsService.updateBodyPart(id, bodyPartDto, principal.getName());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void deleteBodyPart(@PathVariable Long id, Principal principal) {
        bodyPartsService.deleteBodyPart(id, principal.getName());
    }
}

package ch.fhnw.fitnesscounter.controller.coreData;

import ch.fhnw.fitnesscounter.dto.coreData.BodyPartDto;
import ch.fhnw.fitnesscounter.dto.coreData.BodyPartsListDto;
import ch.fhnw.fitnesscounter.service.coreData.BodyPartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/body-part")
@RequiredArgsConstructor
public class BodyPartController {
    private final BodyPartService bodyPartService;

    /**
     *
     * Diese Route gibt alle Körperteile/Muskeln zurück.
     *
     * @return
     */
    @GetMapping("/")
    public List<BodyPartDto> getBodyParts () {
        return bodyPartService.getAllBodyParts();
    }

    /**
     *
     * Diese Route erlaubt das Auslesen eines einzelnen Körperteils
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public BodyPartDto getBodyPartById (@PathVariable Long id, Principal principal) {
        return bodyPartService.getBodyPartById(id, principal.getName());
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
        bodyPartService.createBodyPart(bodyPart, principal.getName());
    }

    /**
     *
     * Mit dieser Route lassen sich mehrere Körperteile gleichzeitig erfassen.
     *
     * @param bodyPartsList
     * @param principal
     */
    @PostMapping("/add-many")
    @ResponseStatus(HttpStatus.CREATED)
    public void createMultipleBodyParts(@Valid @RequestBody BodyPartsListDto bodyPartsList, Principal principal) {
        bodyPartService.createMany(bodyPartsList.getBodyParts(), principal.getName());
    }

    /**
     *
     * Mit dieser Route kann ein Körperteil geändert werden.
     *
     * @param id
     * @param bodyPartDto
     * @param principal
     */
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void updateBodyPart(@PathVariable Long id, @Valid @RequestBody BodyPartDto bodyPartDto, Principal principal) {
        bodyPartService.updateBodyPart(id, bodyPartDto, principal.getName());
    }

    /**
     *
     * Diese Route erlaubt das Löschen eines Körperteils.
     *
     * @param id
     * @param principal
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void deleteBodyPart(@PathVariable Long id, Principal principal) {
        bodyPartService.deleteBodyPart(id, principal.getName());
    }
}

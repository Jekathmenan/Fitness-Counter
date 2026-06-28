package ch.fhnw.fitnesscounter.controller;

import ch.fhnw.fitnesscounter.dto.core.BodyPartDto;
import ch.fhnw.fitnesscounter.model.bodypart.BodyPart;
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
}

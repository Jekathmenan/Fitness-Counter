package ch.fhnw.fitnesscounter.dto.core;

import jakarta.validation.Valid;

import java.util.List;

public record BodyPartsDTO(
        @Valid
        List<BodyPartDto> bodyParts
)
{
}

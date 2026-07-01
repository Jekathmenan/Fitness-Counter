package ch.fhnw.fitnesscounter.dto.coreData;

import jakarta.validation.Valid;
import lombok.Getter;

import java.util.List;

@Getter
public class BodyPartsListDto {
        @Valid
        List<BodyPartDto> bodyParts;
}

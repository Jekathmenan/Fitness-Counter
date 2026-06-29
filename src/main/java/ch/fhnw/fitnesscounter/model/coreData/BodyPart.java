package ch.fhnw.fitnesscounter.model.coreData;

import ch.fhnw.fitnesscounter.dto.core.BodyPartDto;
import ch.fhnw.fitnesscounter.model.BaseEntity;
import ch.fhnw.fitnesscounter.util.DataNormalizer;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name= "bodyparts")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BodyPart extends BaseEntity  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = true)
    private String description;

    /**
     *
     * Diese Helfermethode übernimmt die Werte einer DTO in die eigenen Attribute und unterstützt somit die Update-Route.
     *
     * @param dto
     */
    public void updateFrom(BodyPartDto dto) {
        this.name = DataNormalizer.normalizeString(dto.name());
        this.description = dto.description();
    }

    // TODO: User should be able to request a Body Part. A requested bodypart is available for that user once a admin grants that request that bodypart is available for all users
}

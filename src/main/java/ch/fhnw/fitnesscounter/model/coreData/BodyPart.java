package ch.fhnw.fitnesscounter.model.coreData;

import ch.fhnw.fitnesscounter.dto.coreData.BodyPartDto;
import ch.fhnw.fitnesscounter.model.BaseEntity;
import ch.fhnw.fitnesscounter.util.DataNormalizer;
import jakarta.persistence.*;
import lombok.*;
import java.util.HashSet;
import java.util.Set;

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

    @Builder.Default
    @ManyToMany(mappedBy = "trainedBodyParts")
    private Set<Exercise> exercises = new HashSet<>();

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

    public BodyPartDto toDto() {
        return new BodyPartDto(id, name, description, exercises.isEmpty());
    }

    // TODO: User should be able to request a Body Part. A requested bodypart is available for that user once a admin grants that request that bodypart is available for all users
}

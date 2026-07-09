package ch.fhnw.fitnesscounter.model.coreData;

import ch.fhnw.fitnesscounter.dto.coreData.BodyPartDto;
import ch.fhnw.fitnesscounter.dto.coreData.ExerciseDto;
import ch.fhnw.fitnesscounter.dto.coreData.MovementTypeDto;
import ch.fhnw.fitnesscounter.model.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "exercises")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Exercise extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    private String description;

    @ManyToMany
    @JoinTable (
            name = "exercise_body_parts",
            joinColumns = @JoinColumn(name = "exercise_id"),
            inverseJoinColumns = @JoinColumn(name = "body_part_id")
    )
    private Set<BodyPart> trainedBodyParts = new HashSet<>();

    @ManyToMany
    @JoinTable (
            name = "exercise_movement_type",
            joinColumns = @JoinColumn(name = "exercise_id"),
            inverseJoinColumns = @JoinColumn(name = "movement_type_id")
    )
    private Set<MovementType> movementTypes = new HashSet<>();


    public List<BodyPartDto> getTrainedBodyPartsAsDto () {
        return trainedBodyParts.stream().map(BodyPartDto::fromEntity).toList();
    }

    public List<MovementTypeDto> getMovementTypesAsDto () {
        return movementTypes.stream().map(MovementTypeDto::fromEntity).toList();
    }

    public ExerciseDto toDto() {
        return new ExerciseDto(id, name, description, getTrainedBodyPartsAsDto(), getMovementTypesAsDto());
    }
}

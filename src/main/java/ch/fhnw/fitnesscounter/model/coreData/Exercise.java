package ch.fhnw.fitnesscounter.model.coreData;

import ch.fhnw.fitnesscounter.dto.coreData.BodyPartDto;
import ch.fhnw.fitnesscounter.dto.coreData.ExerciseDto;
import ch.fhnw.fitnesscounter.dto.coreData.MovementTypeDto;
import ch.fhnw.fitnesscounter.model.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import java.util.ArrayList;
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

    @Builder.Default
    @ManyToMany
    @JoinTable (
            name = "exercise_body_parts",
            joinColumns = @JoinColumn(name = "exercise_id"),
            inverseJoinColumns = @JoinColumn(name = "body_part_id")
    )
    private Set<BodyPart> trainedBodyParts = new HashSet<>();

    @Builder.Default
    @ManyToMany
    @JoinTable (
            name = "exercise_movement_type",
            joinColumns = @JoinColumn(name = "exercise_id"),
            inverseJoinColumns = @JoinColumn(name = "movement_type_id")
    )
    private Set<MovementType> movementTypes = new HashSet<>();

    @OneToMany(mappedBy = "exercise", fetch = FetchType.LAZY)
    @Builder.Default
    private List<WorkoutExercise> workoutExercises = new ArrayList<>();

    public List<BodyPartDto> getTrainedBodyPartsAsDto () {
        return trainedBodyParts.stream().map(BodyPartDto::fromEntity).toList();
    }

    public List<MovementTypeDto> getMovementTypesAsDto () {
        return movementTypes.stream().map(MovementTypeDto::fromEntity).toList();
    }

    public ExerciseDto toDto() {
        boolean unused = workoutExercises == null || workoutExercises.isEmpty();
        return new ExerciseDto(id, name, description, getTrainedBodyPartsAsDto(), getMovementTypesAsDto(), unused);
    }
}

package ch.fhnw.fitnesscounter.model.coreData;

import ch.fhnw.fitnesscounter.dto.coreData.MovementTypeDto;
import ch.fhnw.fitnesscounter.model.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Entity
@Table(name = "movement_types")
@Getter
@Setter
@NoArgsConstructor
@Builder
public class MovementType extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = true)
    private String description;

    public MovementType (Long id, String name, String description) {
        this.id = id;
        this.name = name.toUpperCase();
        this.description = description;
    }

    public void setName(String name) {
        this.name =  name.toUpperCase();
    }
}

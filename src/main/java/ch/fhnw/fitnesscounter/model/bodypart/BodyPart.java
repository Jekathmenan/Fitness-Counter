package ch.fhnw.fitnesscounter.model.bodypart;

import ch.fhnw.fitnesscounter.model.BaseEntity;
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

    // TODO: User should be able to request a Body Part. A requested bodypart is available for that user once a admin grants that request that bodypart is available for all users
}

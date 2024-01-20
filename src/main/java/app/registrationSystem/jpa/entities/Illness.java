package app.registrationSystem.jpa.entities;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import java.util.Set;

@Entity
@Table(name = "illness", schema = "registration_system")
@Data
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id"
)
public class Illness {
    @Id
    @Setter(value = AccessLevel.NONE)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String name;

    private int severity;

    @ManyToOne
    @JoinColumn(name = "specialization_id")
    private Specialization specialization;

    private int visitDuration;

    @ManyToMany(mappedBy = "illnesses")
    private Set<Patient> patients;
}

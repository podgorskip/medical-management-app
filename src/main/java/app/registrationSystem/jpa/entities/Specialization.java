package app.registrationSystem.jpa.entities;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "specialization", schema = "registration_system")
@Data
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id"
)
public class Specialization {
    @Id
    @Setter(value = AccessLevel.NONE)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToMany(mappedBy = "specialization")
    private List<Illness> illnesses;

    @OneToMany(mappedBy = "specialization")
    private List<Doctor> doctors;

    @OneToMany(mappedBy = "specialization")
    private List<Question> questions;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(
            name = "medication_specialization",
            joinColumns = @JoinColumn(name = "specialization_id"),
            inverseJoinColumns = @JoinColumn(name = "medication_id")
    )
    private Set<Medication> medications;

    @OneToMany(mappedBy = "specialization")
    private List<VisitHistory> visitHistories;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Specialization that = (Specialization) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

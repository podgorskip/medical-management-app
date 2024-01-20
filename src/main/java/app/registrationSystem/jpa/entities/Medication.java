package app.registrationSystem.jpa.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "medication", schema = "registration_system")
@Data
public class Medication {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private double dose;
    private String description;

    @ManyToMany(mappedBy = "medications")
    private Set<Prescription> prescriptions;

    @ManyToMany(mappedBy = "medications")
    private Set<Specialization> specializations;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Medication that = (Medication) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

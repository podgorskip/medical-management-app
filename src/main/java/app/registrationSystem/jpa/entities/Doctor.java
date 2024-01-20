package app.registrationSystem.jpa.entities;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "doctor", schema = "registration_system")
@Data
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id"
)
@JsonIgnoreProperties({"scheduledVisits", "availableVisits", "prescriptions", "answers"})
public class Doctor {
    @Id
    @Setter(value = AccessLevel.NONE)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "specialization_id")
    private Specialization specialization;

    @OneToMany(mappedBy = "doctor")
    private List<ScheduledVisit> scheduledVisits;

    @OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL)
    private List<AvailableVisit> availableVisits;

    @OneToMany(mappedBy = "doctor")
    private List<Prescription> prescriptions;

    @OneToMany(mappedBy = "doctor")
    private List<Answer> answers;

    @OneToMany(mappedBy = "doctor")
    private List<VisitHistory> visitHistories;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Doctor doctor = (Doctor) o;

        return Objects.equals(id, doctor.id) && user.equals(doctor.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, user);
    }
}

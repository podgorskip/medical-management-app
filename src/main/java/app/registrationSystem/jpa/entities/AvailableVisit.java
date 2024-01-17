package app.registrationSystem.jpa.entities;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "available_visit", schema = "registration_system")
@Data
public class AvailableVisit {
    @Id
    @Setter(value = AccessLevel.NONE)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime date;

    private int duration;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "doctor_id")
    private Doctor doctor;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AvailableVisit that = (AvailableVisit) o;
        return duration == that.duration && Objects.equals(id, that.id) && Objects.equals(date, that.date) && Objects.equals(doctor, that.doctor);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, date, duration, doctor);
    }
}

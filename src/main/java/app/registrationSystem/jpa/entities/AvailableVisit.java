package app.registrationSystem.jpa.entities;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import java.time.LocalDateTime;

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

    @ManyToOne
    @JoinColumn(name = "doctor_id")
    private Doctor doctor;
}

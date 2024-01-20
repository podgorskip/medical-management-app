package app.registrationSystem.jpa.entities;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "scheduled_visit", schema = "registration_system")
@Data
public class ScheduledVisit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "patient_id")
    private Patient patient;

    @ManyToOne
    @JoinColumn(name = "doctor_id")
    private Doctor doctor;

    private LocalDateTime date;

    private int duration;

    @ManyToOne
    @JoinColumn(name = "specialization_id")
    private Specialization specialization;
}

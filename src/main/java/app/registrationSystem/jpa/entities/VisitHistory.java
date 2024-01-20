package app.registrationSystem.jpa.entities;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "visit_history", schema = "registration_system")
@Data
public class VisitHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "patient_id")
    private Patient patient;

    @ManyToOne
    @JoinColumn(name = "doctor_id")
    private Doctor doctor;

    private LocalDateTime date;

    @ManyToOne
    @JoinColumn(name = "specialization_id")
    private Specialization specialization;

    @OneToOne(mappedBy = "visitHistory")
    private Review review;
}

package app.registrationSystem.jpa.entities;

import jakarta.persistence.*;
import lombok.Data;
import java.util.Date;

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

    private Date date;

    private int duration;

    @ManyToOne
    @JoinColumn(name = "illness_id")
    private Illness illness;
}

package app.registrationSystem.jpa.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "prescription", schema = "registration_system")
@Data
public class Prescription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "patient_id")
    private Patient patient;

    @ManyToOne
    @JoinColumn(name = "doctor_id")
    private Doctor doctor;

    private String description;

    @Column(name = "code", unique = true)
    private String code;

    @Column(name = "issue_date")
    private LocalDateTime issueDate;

    @Column(name = "expiration_date")
    private LocalDateTime expirationDate;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(
            name = "prescription_medication",
            joinColumns = @JoinColumn(name = "prescription_id"),
            inverseJoinColumns = @JoinColumn(name = "medication_id")
    )
    private Set<Medication> medications;
}

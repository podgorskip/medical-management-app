package app.registrationSystem.jpa.entities;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "review", schema = "registration_system")
@Data
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "visit_id")
    private VisitHistory visitHistory;

    private int rating;

    private String comment;

    private LocalDateTime date;
}

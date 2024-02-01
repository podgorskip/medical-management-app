package app.registrationSystem.jpa.repositories;

import app.registrationSystem.jpa.entities.AvailableVisit;
import app.registrationSystem.jpa.entities.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface AvailableVisitRepository extends JpaRepository<AvailableVisit, Long> {
    Optional<List<AvailableVisit>> findByDoctor(Doctor doctor);
}

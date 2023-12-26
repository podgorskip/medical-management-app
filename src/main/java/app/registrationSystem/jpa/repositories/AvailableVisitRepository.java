package app.registrationSystem.jpa.repositories;

import app.registrationSystem.jpa.entities.AvailableVisit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AvailableVisitRepository extends JpaRepository<AvailableVisit, Long> {
}

package app.registrationSystem.jpa.repositories;

import app.registrationSystem.jpa.entities.ScheduledVisit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScheduledVisitRepository extends JpaRepository<ScheduledVisit, Long> {
}

package app.registrationSystem.jpa.repositories;

import app.registrationSystem.jpa.entities.ScheduledVisit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ScheduledVisitRepository extends JpaRepository<ScheduledVisit, Long> {
    @Query("SELECT sv FROM ScheduledVisit sv " +
            "WHERE sv.date < :date")
    Optional<List<ScheduledVisit>> findBeforeDate(@Param("date") LocalDateTime date);
}

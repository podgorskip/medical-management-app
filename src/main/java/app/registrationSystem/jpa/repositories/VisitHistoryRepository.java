package app.registrationSystem.jpa.repositories;

import app.registrationSystem.jpa.entities.Doctor;
import app.registrationSystem.jpa.entities.Patient;
import app.registrationSystem.jpa.entities.VisitHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface VisitHistoryRepository extends JpaRepository<VisitHistory, Long> {
    Optional<List<VisitHistory>> findByPatient(Patient patient);

    @Query("SELECT vh FROM VisitHistory vh " +
            "LEFT JOIN vh.review r " +
            "WHERE r IS NULL AND vh.patient = :patient")
    Optional<List<VisitHistory>> findUnreviewedByPatient(Patient patient);

    Optional<List<VisitHistory>> findByDoctor(Doctor doctor);
}

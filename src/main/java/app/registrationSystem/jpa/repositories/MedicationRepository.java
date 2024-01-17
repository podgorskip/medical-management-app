package app.registrationSystem.jpa.repositories;

import app.registrationSystem.jpa.entities.Medication;
import app.registrationSystem.jpa.entities.Specialization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface MedicationRepository extends JpaRepository<Medication, Long> {
    Optional<List<Medication>> findBySpecializations(Specialization specialization);
}

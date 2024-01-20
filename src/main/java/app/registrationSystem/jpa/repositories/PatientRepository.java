package app.registrationSystem.jpa.repositories;

import app.registrationSystem.jpa.entities.Illness;
import app.registrationSystem.jpa.entities.Patient;
import app.registrationSystem.jpa.entities.User;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {

    default void addIllnessToPatient(Long id, Illness illness) {
        Patient patient = findById(id).orElseThrow(() -> new EntityNotFoundException("Patient not found with id: " + id));

        if (patient.getIllnesses() == null) { patient.setIllnesses(Set.of(illness));
        } else { patient.getIllnesses().add(illness); }

        save(patient);
    }

    default void addIllnessesToPatient(Long id, Set<Illness> illnesses) {
        Patient patient = findById(id).orElseThrow(() -> new EntityNotFoundException("Patient not found with id: " + id));

        if (patient.getIllnesses() == null) { patient.setIllnesses(illnesses);
        } else { patient.getIllnesses().addAll(illnesses); }

        save(patient);
    }

    Optional<Patient> findByUser(User user);
}

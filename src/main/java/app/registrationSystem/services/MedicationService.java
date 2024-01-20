package app.registrationSystem.services;

import app.registrationSystem.jpa.entities.Medication;
import app.registrationSystem.jpa.entities.Specialization;
import app.registrationSystem.jpa.repositories.MedicationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

/**
 * A class that handles operations related to Medication entity
 */
@Service
@RequiredArgsConstructor
public class MedicationService {
    private final MedicationRepository medicationRepository;

    /**
     * Retrieves a medication by its ID
     * @param id id of the medication
     * @return Medication if present, empty otherwise
     */
    public Optional<Medication> getByID(Long id) {
        return medicationRepository.findById(id);
    }

    /**
     * Retrieves medications by a specialization
     * @param specialization specialization
     * @return list of Specializations if present, empty otherwise
     */
    public Optional<List<Medication>> getBySpecialization(Specialization specialization) {
        return medicationRepository.findBySpecializations(specialization);
    }
}

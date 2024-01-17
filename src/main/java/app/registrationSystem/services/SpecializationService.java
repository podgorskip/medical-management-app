package app.registrationSystem.services;

import app.registrationSystem.jpa.entities.Specialization;
import app.registrationSystem.jpa.repositories.SpecializationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

/**
 * A class that handles operations related to Specialization entity
 */
@Service
@RequiredArgsConstructor
public class SpecializationService {
    private final SpecializationRepository specializationRepository;

    /**
     * Retrieves all the specializations
     * @return list of Specializations
     */
    public List<Specialization> getAll() {
        return specializationRepository.findAll();
    }

    /**
     * Retrieves a specialization by its ID
     * @param id ID of the specialization
     * @return Specialization if present, empty otherwise
     */
    public Optional<Specialization> getById(Long id) {
        return specializationRepository.findById(id);
    }

    /**
     * Retrieves a specialization by its name
     * @param name name of the specialization
     * @return Specialization if present, empty otherwise
     */
    public Optional<Specialization> getByName(String name) {
        return specializationRepository.findByName(name);
    }
}

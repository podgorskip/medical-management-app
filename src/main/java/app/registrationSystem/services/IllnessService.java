package app.registrationSystem.services;

import app.registrationSystem.dto.response.Response;
import app.registrationSystem.jpa.entities.Illness;
import app.registrationSystem.jpa.entities.Patient;
import app.registrationSystem.jpa.repositories.IllnessRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * A class that handles operations related to Illness entity
 */
@Service
@RequiredArgsConstructor
public class IllnessService {
    private final IllnessRepository illnessRepository;
    private final PatientService patientService;

    /**
     * Retrieves all the illnesses
     * @return list of illnesses
     */
    public List<Illness> getAll() {
        return illnessRepository.findAll();
    }

    /**
     * Retrieves an illness with the specified name
     * @param illnessName illness name
     * @return Illness instance if present, empty otherwise
     */
    public Optional<Illness> getByName(String illnessName) {
        return illnessRepository.findByName(illnessName);
    }

    /**
     * Retrieves an illness with the specified ID
     * @param id id of the illness
     * @return Illness instance if present, empty otherwise
     */
    public Optional<Illness> getById(Long id) {
        return illnessRepository.findById(id);
    }

    /**
     * Returns a set of assigned illnesses to the patient account of the provided username
     * @param username username of the account to have illnesses checked
     * @return set of illnesses assigned to the account
     */
    public Optional<Set<Illness>> checkAssignedIllnesses(String username) {
        Optional<Patient> patient = patientService.getByUsername(username);

        if (patient.isEmpty()) return Optional.empty();

        return Optional.of(patient.get().getIllnesses());
    }

    /**
     * Adds illnesses to the Patient object
     * @param username username of the patient to have illnesses added
     * @param illnesses set of illnesses' id
     * @return response with status of the performed action
     */
    @Transactional
    public Response assignIllnessesToPatient(String username, Set<Long> illnesses) {
        Optional<Patient> optionalPatient = patientService.getByUsername(username);

        if (optionalPatient.isEmpty()) {
            return new Response(false, HttpStatus.NOT_FOUND, "No user of the provided username found");
        }

        Patient patient = optionalPatient.get();

        illnesses.stream()
                .map(this::getById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .forEach(illness -> {
                    Set<Patient> patients = illness.getPatients();
                    patients.add(patient);
                    illness.setPatients(patients);
                });

        return new Response(true, HttpStatus.OK, "Correctly assigned illnesses to the patient account");
    }
}

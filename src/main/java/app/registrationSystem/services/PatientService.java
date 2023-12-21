package app.registrationSystem.services;

import app.registrationSystem.dto.PatientDTO;
import app.registrationSystem.jpa.entities.Illness;
import app.registrationSystem.jpa.entities.Patient;
import app.registrationSystem.jpa.entities.User;
import app.registrationSystem.jpa.repositories.IllnessRepository;
import app.registrationSystem.jpa.repositories.PatientRepository;
import app.registrationSystem.security.Role;
import app.registrationSystem.utils.ValidationUtils;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PatientService {
    private final UserService userService;
    private final PatientRepository patientRepository;
    private final IllnessRepository illnessRepository;
    private final ValidationUtils validationUtils;
    private static final Logger log = LogManager.getLogger(PatientService.class);

    /**
     * Creates a new patient account
     * @param patientDTO DTO containing info about the patient
     * @return patient ID if added successfully
     */
    public Optional<Long> addPatient(PatientDTO patientDTO) {

        Optional<User> user = userService.createUser(patientDTO, Role.PATIENT);

        if (user.isEmpty()) return Optional.empty();

        Patient patient = new Patient();
        patient.setUser(user.get());

        return Optional.of(patientRepository.save(patient).getId());
    }

    /**
     * Retrieves patients by their ID
     * @param id ID of the patient
     * @return Patient object if one of the provided ID exists
     */
    public Optional<Patient> getById(Long id) {
        return patientRepository.findById(id);
    }

    /**
     * Retrieves patients by their username
     * @param username username of the patient
     * @return Patient object if one of the provided username exists
     */
    public Optional<Patient> getByUsername(String username) {
        Optional<User> user = userService.findByUsername(username);

        if (user.isEmpty()) {
            return Optional.empty();
        }

        return patientRepository.findByUser(user.get());
    }

    /**
     * Removes the patient's account
     * @param id id of the patient to have the account removed
     * @return ID of the removed account if successful
     */
    public Optional<Long> removePatient(Long id) {
        Optional<Patient> patient = getById(id);

        if (patient.isEmpty()) {
            return Optional.empty();
        }

        userService.removeUser(patient.get().getUser());
        patientRepository.delete(patient.get());

        return Optional.of(id);
    }

    /**
     * Adds illnesses to the Patient object
     * @param username username of the patient to have illnesses added
     * @param illnesses set of illnesses' id
     * @return ID of the patient if added successfully
     */
    public Optional<Long> addIllnesses(String username, Set<Long> illnesses) {
        Optional<Patient> optionalPatient = getByUsername(username);

        if (optionalPatient.isEmpty()) {
            log.info("No user of the username {} found", username);
            return Optional.empty();
        }

        Patient patient = optionalPatient.get();

        Set<Illness> mappedIllnesses = illnesses.stream()
                .map(illnessRepository::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toSet());

        if (patient.getIllnesses() == null) { patient.setIllnesses(mappedIllnesses); }
        else { patient.getIllnesses().addAll(mappedIllnesses); }

        return Optional.of(patientRepository.save(patient).getId());
    }
}

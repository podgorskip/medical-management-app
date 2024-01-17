package app.registrationSystem.services;

import app.registrationSystem.dto.request.PatientRegistrationRequest;
import app.registrationSystem.dto.response.Response;
import app.registrationSystem.jpa.entities.*;
import app.registrationSystem.jpa.repositories.PatientRepository;
import app.registrationSystem.security.Role;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import java.util.*;

/**
 * A class that handles operations related to Patient entity
 */
@Service
@RequiredArgsConstructor
public class PatientService {
    private final UserService userService;
    private final PatientRepository patientRepository;

    /**
     * Creates a new patient account
     * @param patientDTO DTO containing info about the patient
     * @return response with status of the performed action
     */
    @Transactional
    public Response addPatient(PatientRegistrationRequest patientDTO) {

        Optional<User> user = userService.createUser(patientDTO, Role.PATIENT);

        if (user.isEmpty()) {
            return new Response(false, HttpStatus.CONFLICT, "Provided username is already taken");
        }

        Patient patient = new Patient();
        patient.setUser(user.get());

        patientRepository.save(patient);

        return new Response(true, HttpStatus.CREATED, "Correctly added a patient account");
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
     * @return response with status of the performed action
     */
    @Transactional
    public Response removePatient(Long id) {
        Optional<Patient> patient = getById(id);

        if (patient.isEmpty()) {
            return new Response(false, HttpStatus.NOT_FOUND, "User of the provided username not found");
        }

        userService.removeUser(patient.get().getUser());
        patientRepository.delete(patient.get());

        return new Response(true, HttpStatus.OK, "Correctly removed the patient account");
    }
}

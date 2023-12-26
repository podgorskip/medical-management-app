package app.registrationSystem.services;

import app.registrationSystem.dto.PatientDTO;
import app.registrationSystem.dto.Response;
import app.registrationSystem.jpa.entities.*;
import app.registrationSystem.jpa.repositories.PatientRepository;
import app.registrationSystem.security.Role;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PatientService {
    private final UserService userService;
    private final PatientRepository patientRepository;
    private final IllnessService illnessService;
    private final VisitService visitService;

    /**
     * Creates a new patient account
     * @param patientDTO DTO containing info about the patient
     * @return response with status of the performed action
     */
    @Transactional
    public Response addPatient(PatientDTO patientDTO) {

        Optional<User> user = userService.createUser(patientDTO, Role.PATIENT);

        if (user.isEmpty()) {
            return new Response(false, HttpStatus.CONFLICT, "Provided username is already taken");
        }

        Patient patient = new Patient();
        patient.setUser(user.get());

        patientRepository.save(patient);

        return addIllnesses(patientDTO.getUsername(), patientDTO.getIllnesses());
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

    /**
     * Adds illnesses to the Patient object
     * @param username username of the patient to have illnesses added
     * @param illnesses set of illnesses' id
     * @return response with status of the performed action
     */
    @Transactional
    public Response addIllnesses(String username, Set<Long> illnesses) {
        Optional<Patient> optionalPatient = getByUsername(username);

        if (optionalPatient.isEmpty()) {
            return new Response(false, HttpStatus.NOT_FOUND, "No user of the provided username found");
        }

        Patient patient = optionalPatient.get();

        Set<Illness> mappedIllnesses = illnesses.stream()
                .map(illnessService::getById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toSet());

        if (patient.getIllnesses() == null) { patient.setIllnesses(mappedIllnesses); }
        else { patient.getIllnesses().addAll(mappedIllnesses); }

        patientRepository.save(patient).getId();

        return new Response(true, HttpStatus.OK, "Correctly assigned illnesses to the patient account");
    }

    /**
     * Returns available visits for the illnesses added to the account
     * @param username username of the user for whom available visits are checked
     * @return response with status of the performed action
     */
    public Map<String, List<AvailableVisit>> checkVisitRecommendations(String username) {

        Set<Illness> illnesses = getByUsername(username).get().getIllnesses();

        return illnesses.stream()
                .collect(Collectors.toMap(Illness::getName, illness -> visitService.getAvailableVisits(illness.getName()).get()));

    }

    @Transactional
    public Response scheduleVisit(Long visitID, String username) {
        Optional<AvailableVisit> optionalAvailableVisit = visitService.getAvailableVisit(visitID);

        if (optionalAvailableVisit.isEmpty()) {
            return new Response(false, HttpStatus.NOT_FOUND, "No available visit of the id " + visitID + " found");
        }

        AvailableVisit availableVisit = optionalAvailableVisit.get();

        ScheduledVisit scheduledVisit = new ScheduledVisit();
        scheduledVisit.setPatient(getByUsername(username).get());
        scheduledVisit.setDate(availableVisit.getDate());
        scheduledVisit.setDoctor(availableVisit.getDoctor());
        scheduledVisit.setDuration(availableVisit.getDuration());
        scheduledVisit.setSpecialization(availableVisit.getDoctor().getSpecialization());

        visitService.scheduleVisit(scheduledVisit);
        visitService.deleteAvailableVisit(availableVisit);

        return new Response(true, HttpStatus.OK, "Correctly scheduled visit for " + scheduledVisit.getDate() +
                ", which will take up to " + scheduledVisit.getDuration() + " minutes");
    }

    /**
     * Returns list of scheduled visits for the user of the provided username
     * @param username username of the user to have scheduled visits returned
     * @return list of scheduled visits
     */
    public List<ScheduledVisit> checkScheduledVisits(String username) {
        return getByUsername(username).get().getScheduledVisits();
    }
}

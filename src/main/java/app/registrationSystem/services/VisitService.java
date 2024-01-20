package app.registrationSystem.services;

import app.registrationSystem.dto.request.VisitRequest;
import app.registrationSystem.dto.response.Response;
import app.registrationSystem.jpa.entities.*;
import app.registrationSystem.jpa.repositories.AvailableVisitRepository;
import app.registrationSystem.jpa.repositories.ScheduledVisitRepository;
import app.registrationSystem.jpa.repositories.VisitHistoryRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * A class that handles operations related to AvailableVisit and ScheduledVisit entities
 */
@Service
@RequiredArgsConstructor
public class VisitService {
    private final DoctorService doctorService;
    private final PatientService patientService;
    private final IllnessService illnessService;
    private final AvailableVisitRepository availableVisitRepository;
    private final ScheduledVisitRepository scheduledVisitRepository;
    private final VisitHistoryRepository visitHistoryRepository;
    private final MailSenderService mailSenderService;

    /**
     * Returns available visits based on the illness name and the visit duration needed
     * @param illnessName illness name for which available visits should be found
     * @return list of available visits if found, empty otherwise
     */
    public Optional<List<AvailableVisit>> getAvailableVisits(String illnessName) {
        Optional<Illness> illness = illnessService.getByName(illnessName);

        if (illness.isEmpty())
            return Optional.empty();

        return Optional.of(doctorService.getBySpecialization(illness.get().getSpecialization())
                .stream()
                .flatMap(doctors -> doctors.stream().flatMap(doctor -> doctor.getAvailableVisits().stream()))
                .filter(availableVisit ->  availableVisit.getDuration() >= illness.get().getVisitDuration())
                .collect(Collectors.toList()));
    }

    /**
     * Returns available visit based on the provided id
     * @param id id of the visit to be found
     * @return AvailableVisit instance if exists
     */
    public Optional<AvailableVisit> getAvailableVisit(Long id) {
        return availableVisitRepository.findById(id);
    }

    /**
     * Return ScheduledVisit instance of the provided ID
     * @param id id of the scheduled visit
     * @return scheduled visit
     */
    public Optional<ScheduledVisit> getScheduledVisitByID(Long id) {
        return scheduledVisitRepository.findById(id);
    }

    /**
     * Returns list of scheduled visits for the user of the provided username
     * @param username username of the user to have scheduled visits returned
     * @return list of scheduled visits
     */
    public List<ScheduledVisit> checkPatientScheduledVisits(String username) {
        Optional<Patient> patient = patientService.getByUsername(username);

        if (patient.isEmpty())
            return Collections.emptyList();

        return patient.get().getScheduledVisits();
    }

    /**
     * Allows to schedule a visit
     * @param visitID ID of the available to be set as scheduled
     * @param username username of the user to have the visit scheduled
     * @return response with status of the performed action
     */
    @Transactional
    public Response scheduleVisit(Long visitID, String username) {
        Optional<AvailableVisit> optionalAvailableVisit = getAvailableVisit(visitID);

        if (optionalAvailableVisit.isEmpty()) {
            return new Response(false, HttpStatus.NOT_FOUND, "No available visit of the id " + visitID + " found");
        }

        AvailableVisit availableVisit = optionalAvailableVisit.get();
        ScheduledVisit scheduledVisit = new ScheduledVisit();
        Patient patient = patientService.getByUsername(username).get();

        scheduledVisit.setPatient(patient);
        scheduledVisit.setDate(availableVisit.getDate());
        scheduledVisit.setDoctor(availableVisit.getDoctor());
        scheduledVisit.setDuration(availableVisit.getDuration());
        scheduledVisit.setSpecialization(availableVisit.getDoctor().getSpecialization());

        scheduledVisitRepository.save(scheduledVisit);
        availableVisitRepository.delete(availableVisit);

        sendScheduledVisitNotification(patient, availableVisit);

        return new Response(true, HttpStatus.OK, "Successfully scheduled visit");
    }

    /**
     * Returns available visits for the illnesses added to the account
     * @param username username of the user for whom available visits are checked
     * @return response with status of the performed action
     */
    public Map<String, List<AvailableVisit>> checkVisitRecommendations(String username) {
        Optional<Patient> patient = patientService.getByUsername(username);

        if (patient.isEmpty())
            return Collections.emptyMap();

        Set<Illness> illnesses = patient.get().getIllnesses();

        return illnesses.stream()
                .collect(Collectors.toMap(Illness::getName, illness -> getAvailableVisits(illness.getName()).get()));

    }

    /**
     * Returns a list containing scheduled visits for the account of the provided username
     * @param username username of the account to have scheduled visits checked
     * @return a list containing scheduled visits
     */
    public List<ScheduledVisit> checkScheduledVisits(String username) {
        Optional<Doctor> doctor = doctorService.getByUsername(username);

        if (doctor.isEmpty())
            return Collections.emptyList();

        return doctor.get().getScheduledVisits();
    }

    /**
     * Retrieves all the scheduled visits for a doctor of the specified username
     * @param username username of the Doctor to have visits retrieved
     * @return list of ScheduledVisit instances
     */
    public List<ScheduledVisit> getScheduledVisitsForToday(String username) {
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return checkScheduledVisits(username).stream().filter(visit -> visit.getDate().format(format).equals(LocalDateTime.now().format(format))).toList();
    }

    /**
     * Adds available visits for a doctor account
     * @param username username of the doctor account who has visits added
     * @param visits a list of visits to be added
     */
    @Transactional
    public void addVisits(String username, List<VisitRequest> visits) {
        Doctor doctor = doctorService.getByUsername(username).get();

        List<AvailableVisit> availableVisits = new ArrayList<>(doctor.getAvailableVisits());

        for (VisitRequest visit : visits) {
            AvailableVisit availableVisit = new AvailableVisit();
            availableVisit.setDoctor(doctor);
            availableVisit.setDate(visit.getDate());
            availableVisit.setDuration(visit.getDuration());
            availableVisits.add(availableVisit);
        }

        doctor.setAvailableVisits(availableVisits);
    }

    /**
     * Moves scheduled visits on the date before now to history
     */
    @Transactional
    public void moveVisitsToHistory() {
        Optional<List<ScheduledVisit>> scheduledVisits = scheduledVisitRepository.findBeforeDate(LocalDateTime.now());

        if (scheduledVisits.isEmpty())
            return;

        scheduledVisits.get().forEach(visit -> {
            VisitHistory visitHistory = new VisitHistory();

            visitHistory.setPatient(visit.getPatient());
            visitHistory.setDoctor(visit.getDoctor());
            visitHistory.setDate(visit.getDate());
            visitHistory.setSpecialization(visit.getDoctor().getSpecialization());

            visitHistoryRepository.save(visitHistory);
            scheduledVisitRepository.delete(visit);
        });
    }

    public Optional<List<VisitHistory>> getVisitsWaitingForReview(String username) {
        Optional<Patient> patient = patientService.getByUsername(username);

        if (patient.isEmpty()) return Optional.empty();

        return visitHistoryRepository.findUnreviewedByPatient(patient.get());
    }

    /**
     * Auxiliary method for notifying via email when a new visit is scheduled
     * @param patient patient who scheduled the visit
     * @param visit available visit that was scheduled
     */
    private void sendScheduledVisitNotification(Patient patient, AvailableVisit visit) {
        String message = "Successfully scheduled visit to " + visit.getDoctor().getUser().fullName() +
                " for " + DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm").format(visit.getDate()) +
                "\nThe visit will take approximately " + visit.getDuration() + " minutes";

        mailSenderService.sendNewMail(patient.getUser().getEmail(), "New visit scheduled", message);
    }
}

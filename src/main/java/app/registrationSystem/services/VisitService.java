package app.registrationSystem.services;

import app.registrationSystem.jpa.entities.AvailableVisit;
import app.registrationSystem.jpa.entities.Illness;
import app.registrationSystem.jpa.entities.ScheduledVisit;
import app.registrationSystem.jpa.repositories.AvailableVisitRepository;
import app.registrationSystem.jpa.repositories.ScheduledVisitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VisitService {
    private final DoctorService doctorService;
    private final IllnessService illnessService;
    private final AvailableVisitRepository availableVisitRepository;
    private final ScheduledVisitRepository scheduledVisitRepository;

    /**
     * Returns available visits based on the illness name and the visit duration needed
     * @param illnessName illness name for which available visits should be found
     * @return list of available visits if found, empty otherwise
     */
    public Optional<List<AvailableVisit>> getAvailableVisits(String illnessName) {
        Optional<Illness> illness = illnessService.getByName(illnessName);

        if (illness.isEmpty()) return Optional.empty();

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
     * Deletes the available visit
     * @param availableVisit available visit to be removed
     */
    public void deleteAvailableVisit(AvailableVisit availableVisit) {
        availableVisitRepository.delete(availableVisit);
    }

    /**
     * Saves scheduled visit
     * @param scheduledVisit scheduled visit to be saved
     */
    public void scheduleVisit(ScheduledVisit scheduledVisit) {
        scheduledVisitRepository.save(scheduledVisit);
    }

    /**
     * Return ScheduledVisit instance of the provided ID
     * @param id id of the scheduled visit
     * @return scheduled visit
     */
    public Optional<ScheduledVisit> getScheduledVisitByID(Long id) {
        return scheduledVisitRepository.findById(id);
    }

}

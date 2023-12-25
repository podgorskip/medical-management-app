package app.registrationSystem.services;

import app.registrationSystem.jpa.entities.AvailableVisit;
import app.registrationSystem.jpa.entities.Illness;
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

    public List<AvailableVisit> getAvailableVisits(String illnessName) {
        Optional<Illness> illness = illnessService.getByName(illnessName);

        if (illness.isEmpty()) return Collections.emptyList();

        return doctorService.getBySpecialization(illness.get().getSpecialization()).stream().flatMap(doctors ->
                doctors.stream().flatMap(doctor -> doctor.getAvailableVisits().stream())).collect(Collectors.toList());
    }
}

package app.registrationSystem.controllers;

import app.registrationSystem.dto.mappers.AvailableVisitMapper;
import app.registrationSystem.dto.mappers.DoctorMapper;
import app.registrationSystem.dto.mappers.ScheduledVisitMapper;
import app.registrationSystem.dto.request.IllnessesRequest;
import app.registrationSystem.dto.response.AvailableVisitResponse;
import app.registrationSystem.dto.response.DoctorResponse;
import app.registrationSystem.dto.response.Response;
import app.registrationSystem.dto.response.ScheduledVisitResponse;
import app.registrationSystem.jpa.entities.AvailableVisit;
import app.registrationSystem.jpa.entities.Doctor;
import app.registrationSystem.jpa.entities.Illness;
import app.registrationSystem.jpa.entities.ScheduledVisit;
import app.registrationSystem.security.CustomUserDetails;
import app.registrationSystem.security.Privilege;
import app.registrationSystem.security.RequiredPrivilege;
import app.registrationSystem.services.DoctorService;
import app.registrationSystem.services.PatientService;
import app.registrationSystem.services.VisitService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/patient")
public class PatientController {
    private final PatientService patientService;
    private final DoctorService doctorService;
    private final VisitService visitService;

    @RequiredPrivilege(value = Privilege.ADD_ILLNESS)
    @PostMapping("/add-illnesses/{username}")
    public ResponseEntity<Response> addIllnesses(@NotNull @PathVariable("username") String username, @NotNull @RequestBody IllnessesRequest illnesses) {
        Response response = patientService.addIllnesses(username, illnesses.getIllnesses());
        return ResponseEntity.status(response.httpStatus()).body(response);
    }

    @RequiredPrivilege(value = Privilege.CHECK_DOCTORS)
    @GetMapping("/doctors")
    public ResponseEntity<List<DoctorResponse>> getDoctors(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        return ResponseEntity.status(HttpStatus.OK).body(doctorService.getAll().stream().map(DoctorMapper.INSTANCE::convert).toList());
    }

    @RequiredPrivilege(value = Privilege.CHECK_AVAILABLE_VISITS)
    @GetMapping("/visit-recommendations")
    public ResponseEntity<Map<String, List<AvailableVisitResponse>>> checkVisitRecommendations(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        return ResponseEntity.status(HttpStatus.OK).body(AvailableVisitMapper.INSTANCE.mapToResponseMap(patientService.checkVisitRecommendations(customUserDetails.getUsername())));

    }

    @RequiredPrivilege(value = Privilege.CHECK_AVAILABLE_VISITS)
    @GetMapping("/available-visits")
    public ResponseEntity<List<AvailableVisitResponse>> checkAvailableVisits(@AuthenticationPrincipal CustomUserDetails customUserDetails, @NotNull @RequestParam("illness") String illness) {
        Optional<List<AvailableVisit>> availableVisits = visitService.getAvailableVisits(illness);

        if (availableVisits.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        return ResponseEntity.status(HttpStatus.OK).body(availableVisits.get().stream().map(AvailableVisitMapper.INSTANCE::convert).toList());
    }

    @RequiredPrivilege(value = Privilege.SCHEDULE_VISIT)
    @PostMapping("/schedule-visit")
    public ResponseEntity<Response> scheduleVisit(@AuthenticationPrincipal CustomUserDetails customUserDetails, @NotNull @RequestParam("id") Long id) {
        Response response = patientService.scheduleVisit(id, customUserDetails.getUsername());
        return ResponseEntity.status(response.httpStatus()).body(response);
    }

    @RequiredPrivilege(value = Privilege.SCHEDULE_VISIT)
    @GetMapping("/scheduled-visits")
    public ResponseEntity<List<ScheduledVisitResponse>> checkScheduledVisits(@AuthenticationPrincipal CustomUserDetails CustomUserDetails) {
        return ResponseEntity.status(HttpStatus.OK).body(patientService.checkScheduledVisits(CustomUserDetails.getUsername()).stream().map(ScheduledVisitMapper.INSTANCE::convert).toList());
    }

    @RequiredPrivilege(value = Privilege.CHECK_DOCTORS)
    @GetMapping("/doctors-by-specialization/{specialization}")
    public ResponseEntity<List<DoctorResponse>> checkDoctorsBySpecialization(@AuthenticationPrincipal CustomUserDetails customUserDetails, @PathVariable("specialization") String specialization) {
        Optional<List<Doctor>> doctors = doctorService.getBySpecialization(specialization);

        if (doctors.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        return ResponseEntity.status(HttpStatus.OK).body(doctors.get().stream().map(DoctorMapper.INSTANCE::convert).toList());
    }

    @RequiredPrivilege(value = Privilege.CHECK_DOCTORS)
    @GetMapping("/doctors-by-illness/{illness}")
    public ResponseEntity<List<DoctorResponse>> checkDoctorsByIllness(@AuthenticationPrincipal CustomUserDetails customUserDetails, @PathVariable("illness") String illness) {
        Optional<List<Doctor>> doctors = doctorService.getByIllness(illness);

        if (doctors.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        return ResponseEntity.status(HttpStatus.OK).body(doctors.get().stream().map(DoctorMapper.INSTANCE::convert).toList());
    }

    @RequiredPrivilege(value = Privilege.CHECK_ASSIGNED_ILLNESSES)
    @GetMapping("/my-illnesses")
    public ResponseEntity<Set<Illness>> checkAssignedIllnesses(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        return ResponseEntity.status(HttpStatus.OK).body(patientService.checkAssignedIllnesses(customUserDetails.getUsername()));
    }

}

package app.registrationSystem.controllers;

import app.registrationSystem.dto.mappers.AvailableVisitMapper;
import app.registrationSystem.dto.mappers.ScheduledVisitMapper;
import app.registrationSystem.dto.mappers.VisitHistoryMapper;
import app.registrationSystem.dto.request.VisitsRequest;
import app.registrationSystem.dto.response.AvailableVisitResponse;
import app.registrationSystem.dto.response.Response;
import app.registrationSystem.dto.response.ScheduledVisitResponse;
import app.registrationSystem.dto.response.VisitHistoryResponse;
import app.registrationSystem.jpa.entities.AvailableVisit;
import app.registrationSystem.jpa.entities.ScheduledVisit;
import app.registrationSystem.jpa.entities.VisitHistory;
import app.registrationSystem.security.CustomUserDetails;
import app.registrationSystem.security.Privilege;
import app.registrationSystem.security.RequiredPrivilege;
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

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class VisitController {
    private final VisitService visitService;

    @RequiredPrivilege(value = Privilege.ADD_VISIT)
    @PostMapping("/doctor/add-visits")
    public ResponseEntity<String> addVisits(@AuthenticationPrincipal CustomUserDetails customUserDetails, @NotNull @RequestBody VisitsRequest visits) {
        visitService.addVisits(customUserDetails.getUsername(), visits.getVisits());
        return ResponseEntity.status(HttpStatus.OK).body("Correctly added available visits");
    }

    @RequiredPrivilege(value = Privilege.CHECK_SCHEDULED_VISITS)
    @GetMapping("/doctor/scheduled-visits")
    public ResponseEntity<List<ScheduledVisitResponse>> checkScheduledVisits(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        List<ScheduledVisit> scheduledVisits = visitService.checkScheduledVisits(customUserDetails.getUsername());

        if (scheduledVisits.isEmpty())
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

        return ResponseEntity.status(HttpStatus.OK).body(scheduledVisits.stream().map(ScheduledVisitMapper.INSTANCE::convert).toList());
    }

    @RequiredPrivilege(value = Privilege.CHECK_SCHEDULED_VISITS)
    @GetMapping("/doctor/today-visits")
    public ResponseEntity<List<ScheduledVisitResponse>> checkScheduledVisitsForToday(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        List<ScheduledVisit> scheduledVisits = visitService.getScheduledVisitsForToday(customUserDetails.getUsername());

        if (scheduledVisits.isEmpty())
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

        return ResponseEntity.status(HttpStatus.OK).body(scheduledVisits.stream().map(ScheduledVisitMapper.INSTANCE::convert).toList());
    }

    @RequiredPrivilege(value = Privilege.CHECK_AVAILABLE_VISITS)
    @GetMapping("/patient/visit-recommendations")
    public ResponseEntity<Map<String, List<AvailableVisitResponse>>> checkVisitRecommendations(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        Map<String, List<AvailableVisit>> recommendations = visitService.checkVisitRecommendations(customUserDetails.getUsername());

        if (recommendations.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

        return ResponseEntity.status(HttpStatus.OK).body(AvailableVisitMapper.INSTANCE.mapToResponseMap(recommendations));
    }

    @GetMapping("/auth/available-visits")
    public ResponseEntity<List<AvailableVisitResponse>> checkAvailableVisits(@AuthenticationPrincipal CustomUserDetails customUserDetails, @NotNull @RequestParam("specialist") Long id) {
        Optional<List<AvailableVisit>> availableVisits = visitService.getAvailableVisitsByDoctorID(id);

        if (availableVisits.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }

        return ResponseEntity.status(HttpStatus.OK).body(availableVisits.get().stream().map(AvailableVisitMapper.INSTANCE::convert).toList());
    }

    @RequiredPrivilege(value = Privilege.SCHEDULE_VISIT)
    @PostMapping("/patient/schedule-visit")
    public ResponseEntity<Response> scheduleVisit(@AuthenticationPrincipal CustomUserDetails customUserDetails, @NotNull @RequestParam("id") Long id) {
        Response response = visitService.scheduleVisit(id, customUserDetails.getUsername());
        return ResponseEntity.status(response.httpStatus()).body(response);
    }

    @RequiredPrivilege(value = Privilege.SCHEDULE_VISIT)
    @GetMapping("/patient/scheduled-visits")
    public ResponseEntity<List<ScheduledVisitResponse>> checkPatientScheduledVisits(@AuthenticationPrincipal CustomUserDetails CustomUserDetails) {
        List<ScheduledVisit> scheduledVisits = visitService.checkPatientScheduledVisits(CustomUserDetails.getUsername());

        if (scheduledVisits.isEmpty())
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

        return ResponseEntity.status(HttpStatus.OK).body(scheduledVisits.stream().map(ScheduledVisitMapper.INSTANCE::convert).toList());
    }

    @RequiredPrivilege(value = Privilege.CHECK_VISIT_HISTORY)
    @GetMapping("/patient/visit-history")
    public ResponseEntity<List<VisitHistoryResponse>> checkVisitHistory(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        Optional<List<VisitHistory>> visitHistories = visitService.getVisitHistoriesByPatientUsername(customUserDetails.getUsername());

        if (visitHistories.isEmpty())
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

        return ResponseEntity.status(HttpStatus.OK).body(visitHistories.get().stream().map(VisitHistoryMapper.INSTANCE::convert).toList());
    }
}

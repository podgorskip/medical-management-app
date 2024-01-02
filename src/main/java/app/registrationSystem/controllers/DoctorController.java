package app.registrationSystem.controllers;

import app.registrationSystem.dto.mappers.ScheduledVisitMapper;
import app.registrationSystem.dto.request.VisitsRequest;
import app.registrationSystem.dto.response.ScheduledVisitResponse;
import app.registrationSystem.jpa.entities.ScheduledVisit;
import app.registrationSystem.security.CustomUserDetails;
import app.registrationSystem.security.Privilege;
import app.registrationSystem.security.RequiredPrivilege;
import app.registrationSystem.services.DoctorService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/doctor")
@RequiredArgsConstructor
public class DoctorController {
    private final DoctorService doctorService;

    @RequiredPrivilege(value = Privilege.ADD_VISIT)
    @PostMapping("/add-visits")
    public ResponseEntity<String> addVisits(@AuthenticationPrincipal CustomUserDetails customUserDetails, @NotNull @RequestBody VisitsRequest visits) {
        doctorService.addVisits(customUserDetails.getUsername(), visits.getVisits());
        return ResponseEntity.status(HttpStatus.OK).body("Correctly added available visits");
    }

    @RequiredPrivilege(value = Privilege.CHECK_SCHEDULED_VISITS)
    @GetMapping("/scheduled-visits")
    public ResponseEntity<List<ScheduledVisitResponse>> checkScheduledVisits(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        return ResponseEntity.status(HttpStatus.OK).body(doctorService.checkScheduledVisits(customUserDetails.getUsername()).stream().map(ScheduledVisitMapper.INSTANCE::convert).toList());
    }


}

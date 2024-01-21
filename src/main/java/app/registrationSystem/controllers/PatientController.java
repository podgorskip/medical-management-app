package app.registrationSystem.controllers;

import app.registrationSystem.dto.response.Response;
import app.registrationSystem.security.CustomUserDetails;
import app.registrationSystem.security.Privilege;
import app.registrationSystem.security.RequiredPrivilege;
import app.registrationSystem.services.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/patient")
public class PatientController {
    private final PatientService patientService;

    @RequiredPrivilege(value = Privilege.REMOVE_PATIENT)
    @DeleteMapping("/remove-patient/{id}")
    public ResponseEntity<Response> removePatient(@AuthenticationPrincipal CustomUserDetails customUserDetails, @PathVariable("id") Long id) {
        Response response = patientService.removePatient(id);
        return ResponseEntity.status(response.httpStatus()).body(response);
    }
}

package app.registrationSystem.controllers;

import app.registrationSystem.dto.DoctorDTO;
import app.registrationSystem.dto.Response;
import app.registrationSystem.security.CustomUserDetails;
import app.registrationSystem.security.Privilege;
import app.registrationSystem.security.RequiredPrivilege;
import app.registrationSystem.services.DoctorService;
import app.registrationSystem.services.PatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {
    private final DoctorService doctorService;
    private final PatientService patientService;

    @RequiredPrivilege(value = Privilege.REMOVE_DOCTOR)
    @DeleteMapping("/remove-doctor/{id}")
    public ResponseEntity<Response> removeDoctor(@AuthenticationPrincipal CustomUserDetails customUserDetails, @PathVariable("id") Long id) {
        Response response = doctorService.removeDoctor(id);
        return ResponseEntity.status(response.httpStatus()).body(response);
    }

    @RequiredPrivilege(value = Privilege.REMOVE_PATIENT)
    @DeleteMapping("/remove-patient/{id}")
    public ResponseEntity<Response> removePatient(@AuthenticationPrincipal CustomUserDetails customUserDetails, @PathVariable("id") Long id) {
        Response response = patientService.removePatient(id);
        return ResponseEntity.status(response.httpStatus()).body(response);
    }

    @RequiredPrivilege(value = Privilege.ADD_DOCTOR)
    @PostMapping("/add-doctor")
    public ResponseEntity<Response> addDoctor(@AuthenticationPrincipal CustomUserDetails customUserDetails, @RequestBody DoctorDTO doctorDTO){
        Response response = doctorService.addDoctor(doctorDTO);
        return ResponseEntity.status(response.httpStatus()).body(response);
    }
}

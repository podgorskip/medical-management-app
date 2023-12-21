package app.registrationSystem.controllers;

import app.registrationSystem.dto.DoctorDTO;
import app.registrationSystem.security.CustomUserDetails;
import app.registrationSystem.security.Privilege;
import app.registrationSystem.security.RequiredPrivilege;
import app.registrationSystem.services.DoctorService;
import app.registrationSystem.services.PatientService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AdminController {
    private final DoctorService doctorService;
    private final PatientService patientService;

    @Transactional
    @RequiredPrivilege(value = Privilege.REMOVE_DOCTOR)
    @DeleteMapping("/remove-doctor/{id}")
    public ResponseEntity<String> removeDoctor(@AuthenticationPrincipal CustomUserDetails customUserDetails, @PathVariable("id") Long id) {

        Optional<Long> doctorID = doctorService.removeDoctor(id);

        if (doctorID.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No user of the id " + id + " found");
        }

        return ResponseEntity.status(HttpStatus.OK).body("Correctly removed the user of the id " + id);
    }

    @Transactional
    @RequiredPrivilege(value = Privilege.REMOVE_PATIENT)
    @DeleteMapping("/remove-patient/{id}")
    public ResponseEntity<String> removePatient(@AuthenticationPrincipal CustomUserDetails customUserDetails, @PathVariable("id") Long id) {

        Optional<Long> patientID = patientService.removePatient(id);

        if (patientID.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No user of the id " + id + " found");
        }

        return ResponseEntity.status(HttpStatus.OK).body("Correctly removed the user of the id " + id);
    }

    @Transactional
    @RequiredPrivilege(value = Privilege.ADD_DOCTOR)
    @PostMapping("/add-doctor")
    public ResponseEntity<String> addDoctor(@AuthenticationPrincipal CustomUserDetails customUserDetails, @RequestBody DoctorDTO doctorDTO){

        Optional<Long> doctorID = doctorService.addDoctor(doctorDTO);

        if (doctorID.isEmpty()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Username: " + doctorDTO.getUsername() + " is already taken");
        }

        return ResponseEntity.status(HttpStatus.OK).body("Correctly created a user account");
    }
}

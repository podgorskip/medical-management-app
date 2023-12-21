package app.registrationSystem.controllers;

import app.registrationSystem.dto.IllnessesDTO;
import app.registrationSystem.dto.PatientDTO;
import app.registrationSystem.jpa.entities.Doctor;
import app.registrationSystem.security.CustomUserDetails;
import app.registrationSystem.security.Privilege;
import app.registrationSystem.security.RequiredPrivilege;
import app.registrationSystem.services.DoctorService;
import app.registrationSystem.services.PatientService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class PatientController {
    private final PatientService patientService;
    private final DoctorService doctorService;

    @Transactional
    @PostMapping("/auth/register")
    public ResponseEntity<String> register(@Valid @RequestBody PatientDTO patientDTO) {

        Optional<Long> id = patientService.addPatient(patientDTO);

        if (id.isEmpty()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Username: " + patientDTO.getUsername() + " is already taken");
        }

        return ResponseEntity.ok("Correctly added an user of id: " + id);
    }

    @Transactional
    @RequiredPrivilege(value = Privilege.ADD_ILLNESS)
    @PostMapping("/add-illnesses/{username}")
    public ResponseEntity<String> addIllnesses(@PathVariable("username") String username, @RequestBody IllnessesDTO illnesses) {

        Optional<Long> id = patientService.addIllnesses(username, illnesses.getIllnesses());

        if (id.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No user of username: " + username + " found");
        }

        return ResponseEntity.ok("Correctly added illnesses to user of username: " + username);
    }

    @RequiredPrivilege(value = Privilege.CHECK_DOCTORS)
    @GetMapping("/check-doctors")
    public ResponseEntity<List<Doctor>> getDoctors(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        return ResponseEntity.status(HttpStatus.OK).body(doctorService.getAll());
    }

}

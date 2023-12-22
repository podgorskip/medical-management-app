package app.registrationSystem.controllers;

import app.registrationSystem.dto.IllnessesDTO;
import app.registrationSystem.dto.PatientDTO;
import app.registrationSystem.dto.Response;
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
    public ResponseEntity<Response> register(@Valid @RequestBody PatientDTO patientDTO) {
        Response response = patientService.addPatient(patientDTO);
        return ResponseEntity.status(response.httpStatus()).body(response);
    }

    @Transactional
    @RequiredPrivilege(value = Privilege.ADD_ILLNESS)
    @PostMapping("/add-illnesses/{username}")
    public ResponseEntity<Response> addIllnesses(@PathVariable("username") String username, @RequestBody IllnessesDTO illnesses) {
        Response response = patientService.addIllnesses(username, illnesses.getIllnesses());
        return ResponseEntity.status(response.httpStatus()).body(response);
    }

    @RequiredPrivilege(value = Privilege.CHECK_DOCTORS)
    @GetMapping("/check-doctors")
    public ResponseEntity<List<Doctor>> getDoctors(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        return ResponseEntity.status(HttpStatus.OK).body(doctorService.getAll());
    }

}

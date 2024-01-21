package app.registrationSystem.controllers;

import app.registrationSystem.dto.mappers.*;
import app.registrationSystem.dto.request.DoctorRegistrationRequest;
import app.registrationSystem.dto.response.*;
import app.registrationSystem.jpa.entities.*;
import app.registrationSystem.security.CustomUserDetails;
import app.registrationSystem.security.Privilege;
import app.registrationSystem.security.RequiredPrivilege;
import app.registrationSystem.services.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class DoctorController {
    private final DoctorService doctorService;

    @RequiredPrivilege(value = Privilege.CHECK_DOCTORS)
    @GetMapping("/patient/doctors")
    public ResponseEntity<List<DoctorResponse>> getDoctors(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        return ResponseEntity.status(HttpStatus.OK).body(doctorService.getAll().stream().map(DoctorMapper.INSTANCE::convert).toList());
    }

    @RequiredPrivilege(value = Privilege.CHECK_DOCTORS)
    @GetMapping("/patient/doctors-by-specialization/{specialization}")
    public ResponseEntity<List<DoctorResponse>> checkDoctorsBySpecialization(@AuthenticationPrincipal CustomUserDetails customUserDetails, @PathVariable("specialization") String specialization) {
        Optional<List<Doctor>> doctors = doctorService.getBySpecialization(specialization);

        if (doctors.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        return ResponseEntity.status(HttpStatus.OK).body(doctors.get().stream().map(DoctorMapper.INSTANCE::convert).toList());
    }

    @RequiredPrivilege(value = Privilege.CHECK_DOCTORS)
    @GetMapping("/patient/doctors-by-illness/{illness}")
    public ResponseEntity<List<DoctorResponse>> checkDoctorsByIllness(@AuthenticationPrincipal CustomUserDetails customUserDetails, @PathVariable("illness") String illness) {
        Optional<List<Doctor>> doctors = doctorService.getByIllness(illness);

        if (doctors.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        return ResponseEntity.status(HttpStatus.OK).body(doctors.get().stream().map(DoctorMapper.INSTANCE::convert).toList());
    }

    @RequiredPrivilege(value = Privilege.REMOVE_DOCTOR)
    @DeleteMapping("/admin/remove-doctor/{id}")
    public ResponseEntity<Response> removeDoctor(@AuthenticationPrincipal CustomUserDetails customUserDetails, @PathVariable("id") Long id) {
        Response response = doctorService.removeDoctor(id);
        return ResponseEntity.status(response.httpStatus()).body(response);
    }

    @RequiredPrivilege(value = Privilege.ADD_DOCTOR)
    @PostMapping("/add-doctor")
    public ResponseEntity<Response> addDoctor(@AuthenticationPrincipal CustomUserDetails customUserDetails, @RequestBody DoctorRegistrationRequest doctorDTO){
        Response response = doctorService.addDoctor(doctorDTO);
        return ResponseEntity.status(response.httpStatus()).body(response);
    }
}

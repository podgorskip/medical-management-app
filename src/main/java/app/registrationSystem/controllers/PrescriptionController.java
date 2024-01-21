package app.registrationSystem.controllers;

import app.registrationSystem.dto.mappers.MedicationMapper;
import app.registrationSystem.dto.mappers.PrescriptionMapper;
import app.registrationSystem.dto.request.PrescriptionRequest;
import app.registrationSystem.dto.response.MedicationResponse;
import app.registrationSystem.dto.response.PrescriptionResponse;
import app.registrationSystem.dto.response.Response;
import app.registrationSystem.jpa.entities.Medication;
import app.registrationSystem.jpa.entities.Prescription;
import app.registrationSystem.security.CustomUserDetails;
import app.registrationSystem.security.Privilege;
import app.registrationSystem.security.RequiredPrivilege;
import app.registrationSystem.services.DoctorService;
import app.registrationSystem.services.MedicationService;
import app.registrationSystem.services.PrescriptionService;
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
public class PrescriptionController {
    private final PrescriptionService prescriptionService;
    private final MedicationService medicationService;
    private final DoctorService doctorService;

    @RequiredPrivilege(value = Privilege.PRESCRIBE_MEDICATIONS)
    @PostMapping("doctor//prescribe-medications")
    public ResponseEntity<Response> prescribeMedications(@AuthenticationPrincipal CustomUserDetails customUserDetails, @RequestBody @Valid PrescriptionRequest prescriptionRequest) {
        Response response = prescriptionService.addPrescription(prescriptionRequest, customUserDetails.getUsername());
        return ResponseEntity.status(response.httpStatus()).body(response);
    }

    @RequiredPrivilege(value = Privilege.CHECK_MEDICATIONS)
    @GetMapping("/doctor/medications")
    public ResponseEntity<List<MedicationResponse>> checkMedications(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        Optional<List<Medication>> medications = medicationService.getBySpecialization(doctorService.getByUsername(customUserDetails.getUsername()).get().getSpecialization());

        if (medications.isEmpty()) return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

        return ResponseEntity.status(HttpStatus.OK).body(medications.get().stream().map(MedicationMapper.INSTANCE::convert).toList());
    }

    @RequiredPrivilege(value = Privilege.CHECK_PRESCRIPTIONS)
    @GetMapping("/doctor/prescriptions")
    public ResponseEntity<List<PrescriptionResponse>> checkIssuedPrescriptions(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        List<Prescription> prescriptions = prescriptionService.checkIssuedPrescriptions(customUserDetails.getUsername());
        if (prescriptions.isEmpty()) return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

        return ResponseEntity.status(HttpStatus.OK).body(prescriptions.stream().map(PrescriptionMapper.INSTANCE::convert).toList());
    }

    @RequiredPrivilege(value = Privilege.CHECK_PRESCRIPTIONS)
    @GetMapping("/patient/my-prescriptions")
    public ResponseEntity<List<PrescriptionResponse>> checkAssignedPrescriptions(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        List<Prescription> prescriptions = prescriptionService.checkPrescriptions(customUserDetails.getUsername());

        if (prescriptions.isEmpty())
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

        return ResponseEntity.status(HttpStatus.OK).body(prescriptions.stream().map(PrescriptionMapper.INSTANCE::convert).toList());
    }
}

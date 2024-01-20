package app.registrationSystem.controllers;

import app.registrationSystem.dto.mappers.MedicationMapper;
import app.registrationSystem.dto.mappers.PrescriptionMapper;
import app.registrationSystem.dto.mappers.QuestionMapper;
import app.registrationSystem.dto.mappers.ScheduledVisitMapper;
import app.registrationSystem.dto.request.AnswerRequest;
import app.registrationSystem.dto.request.PrescriptionRequest;
import app.registrationSystem.dto.request.VisitsRequest;
import app.registrationSystem.dto.response.*;
import app.registrationSystem.jpa.entities.Medication;
import app.registrationSystem.jpa.entities.Prescription;
import app.registrationSystem.jpa.entities.Question;
import app.registrationSystem.jpa.entities.ScheduledVisit;
import app.registrationSystem.security.CustomUserDetails;
import app.registrationSystem.security.Privilege;
import app.registrationSystem.security.RequiredPrivilege;
import app.registrationSystem.services.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/doctor")
@RequiredArgsConstructor
public class DoctorController {
    private final DoctorService doctorService;
    private final PrescriptionService prescriptionService;
    private final MedicationService medicationService;
    private final QuestionService questionService;
    private final AnswerService answerService;
    private final VisitService visitService;

    @RequiredPrivilege(value = Privilege.ADD_VISIT)
    @PostMapping("/add-visits")
    public ResponseEntity<String> addVisits(@AuthenticationPrincipal CustomUserDetails customUserDetails, @NotNull @RequestBody VisitsRequest visits) {
        visitService.addVisits(customUserDetails.getUsername(), visits.getVisits());
        return ResponseEntity.status(HttpStatus.OK).body("Correctly added available visits");
    }

    @RequiredPrivilege(value = Privilege.CHECK_SCHEDULED_VISITS)
    @GetMapping("/scheduled-visits")
    public ResponseEntity<List<ScheduledVisitResponse>> checkScheduledVisits(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        List<ScheduledVisit> scheduledVisits = visitService.checkScheduledVisits(customUserDetails.getUsername());

        if (scheduledVisits.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

        return ResponseEntity.status(HttpStatus.OK).body(scheduledVisits.stream().map(ScheduledVisitMapper.INSTANCE::convert).toList());
    }

    @RequiredPrivilege(value = Privilege.PRESCRIBE_MEDICATIONS)
    @PostMapping("/prescribe-medications")
    public ResponseEntity<Response> prescribeMedications(@AuthenticationPrincipal CustomUserDetails customUserDetails, @RequestBody @Valid PrescriptionRequest prescriptionRequest) {
        Response response = prescriptionService.addPrescription(prescriptionRequest, customUserDetails.getUsername());
        return ResponseEntity.status(response.httpStatus()).body(response);
    }

    @RequiredPrivilege(value = Privilege.CHECK_SCHEDULED_VISITS)
    @GetMapping("/today-visits")
    public ResponseEntity<List<ScheduledVisitResponse>> checkScheduledVisitsForToday(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        List<ScheduledVisit> scheduledVisits = visitService.getScheduledVisitsForToday(customUserDetails.getUsername());

        if (scheduledVisits.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

        return ResponseEntity.status(HttpStatus.OK).body(scheduledVisits.stream().map(ScheduledVisitMapper.INSTANCE::convert).toList());
    }

    @RequiredPrivilege(value = Privilege.CHECK_MEDICATIONS)
    @GetMapping("/medications")
    public ResponseEntity<List<MedicationResponse>> checkMedications(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        Optional<List<Medication>> medications = medicationService.getBySpecialization(doctorService.getByUsername(customUserDetails.getUsername()).get().getSpecialization());

        if (medications.isEmpty()) return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

        return ResponseEntity.status(HttpStatus.OK).body(medications.get().stream().map(MedicationMapper.INSTANCE::convert).toList());
    }

    @RequiredPrivilege(value = Privilege.CHECK_PRESCRIPTIONS)
    @GetMapping("/prescriptions")
    public ResponseEntity<List<PrescriptionResponse>> checkIssuedPrescriptions(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        List<Prescription> prescriptions = prescriptionService.checkIssuedPrescriptions(customUserDetails.getUsername());
        if (prescriptions.isEmpty()) return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

        return ResponseEntity.status(HttpStatus.OK).body(prescriptions.stream().map(PrescriptionMapper.INSTANCE::convert).toList());
    }

    @RequiredPrivilege(value = Privilege.ANSWER_QUESTION)
    @GetMapping("/questions")
    public ResponseEntity<List<QuestionResponse>> checkUnansweredQuestions(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        Optional<List<Question>> questions = questionService.getQuestionsToAnswer(customUserDetails.getUsername());

        if (questions.isEmpty()) return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

        return ResponseEntity.status(HttpStatus.OK).body(questions.get().stream().map(QuestionMapper.INSTANCE::convert).toList());
    }

    @RequiredPrivilege(value = Privilege.ANSWER_QUESTION)
    @PostMapping("/answer-question")
    public ResponseEntity<Response> answerQuestion(@AuthenticationPrincipal CustomUserDetails customUserDetails, @RequestParam("id") Long id, @RequestBody AnswerRequest answerRequest) {
        Response response = answerService.answerQuestion(id, customUserDetails.getUsername(), answerRequest);
        return ResponseEntity.status(response.httpStatus()).body(response);
    }

}

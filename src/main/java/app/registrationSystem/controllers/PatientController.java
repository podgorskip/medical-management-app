package app.registrationSystem.controllers;

import app.registrationSystem.dto.mappers.*;
import app.registrationSystem.dto.request.IllnessesRequest;
import app.registrationSystem.dto.request.PrescriptionRequest;
import app.registrationSystem.dto.request.QuestionRequest;
import app.registrationSystem.dto.response.*;
import app.registrationSystem.jpa.entities.*;
import app.registrationSystem.security.CustomUserDetails;
import app.registrationSystem.security.Privilege;
import app.registrationSystem.security.RequiredPrivilege;
import app.registrationSystem.services.*;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/patient")
public class PatientController {
    private final PatientService patientService;
    private final DoctorService doctorService;
    private final VisitService visitService;
    private final PrescriptionService prescriptionService;
    private final QuestionService questionService;
    private final AnswerService answerService;
    private final IllnessService illnessService;

    @RequiredPrivilege(value = Privilege.ADD_ILLNESS)
    @PostMapping("/add-illnesses/{username}")
    public ResponseEntity<Response> addIllnesses(@NotNull @PathVariable("username") String username, @NotNull @RequestBody IllnessesRequest illnesses) {
        Response response = illnessService.assignIllnessesToPatient(username, illnesses.getIllnesses());
        return ResponseEntity.status(response.httpStatus()).body(response);
    }

    @RequiredPrivilege(value = Privilege.CHECK_DOCTORS)
    @GetMapping("/doctors")
    public ResponseEntity<List<DoctorResponse>> getDoctors(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        return ResponseEntity.status(HttpStatus.OK).body(doctorService.getAll().stream().map(DoctorMapper.INSTANCE::convert).toList());
    }

    @RequiredPrivilege(value = Privilege.CHECK_AVAILABLE_VISITS)
    @GetMapping("/visit-recommendations")
    public ResponseEntity<Map<String, List<AvailableVisitResponse>>> checkVisitRecommendations(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        Map<String, List<AvailableVisit>> recommendations = visitService.checkVisitRecommendations(customUserDetails.getUsername());

        if (recommendations.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

        return ResponseEntity.status(HttpStatus.OK).body(AvailableVisitMapper.INSTANCE.mapToResponseMap(recommendations));
    }

    @RequiredPrivilege(value = Privilege.CHECK_AVAILABLE_VISITS)
    @GetMapping("/available-visits")
    public ResponseEntity<List<AvailableVisitResponse>> checkAvailableVisits(@AuthenticationPrincipal CustomUserDetails customUserDetails, @NotNull @RequestParam("illness") String illness) {
        Optional<List<AvailableVisit>> availableVisits = visitService.getAvailableVisits(illness);

        if (availableVisits.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        return ResponseEntity.status(HttpStatus.OK).body(availableVisits.get().stream().map(AvailableVisitMapper.INSTANCE::convert).toList());
    }

    @RequiredPrivilege(value = Privilege.SCHEDULE_VISIT)
    @PostMapping("/schedule-visit")
    public ResponseEntity<Response> scheduleVisit(@AuthenticationPrincipal CustomUserDetails customUserDetails, @NotNull @RequestParam("id") Long id) {
        Response response = visitService.scheduleVisit(id, customUserDetails.getUsername());
        return ResponseEntity.status(response.httpStatus()).body(response);
    }

    @RequiredPrivilege(value = Privilege.SCHEDULE_VISIT)
    @GetMapping("/scheduled-visits")
    public ResponseEntity<List<ScheduledVisitResponse>> checkScheduledVisits(@AuthenticationPrincipal CustomUserDetails CustomUserDetails) {
        List<ScheduledVisit> scheduledVisits = visitService.checkPatientScheduledVisits(CustomUserDetails.getUsername());

        if (scheduledVisits.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

        return ResponseEntity.status(HttpStatus.OK).body(scheduledVisits.stream().map(ScheduledVisitMapper.INSTANCE::convert).toList());
    }

    @RequiredPrivilege(value = Privilege.CHECK_DOCTORS)
    @GetMapping("/doctors-by-specialization/{specialization}")
    public ResponseEntity<List<DoctorResponse>> checkDoctorsBySpecialization(@AuthenticationPrincipal CustomUserDetails customUserDetails, @PathVariable("specialization") String specialization) {
        Optional<List<Doctor>> doctors = doctorService.getBySpecialization(specialization);

        if (doctors.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        return ResponseEntity.status(HttpStatus.OK).body(doctors.get().stream().map(DoctorMapper.INSTANCE::convert).toList());
    }

    @RequiredPrivilege(value = Privilege.CHECK_DOCTORS)
    @GetMapping("/doctors-by-illness/{illness}")
    public ResponseEntity<List<DoctorResponse>> checkDoctorsByIllness(@AuthenticationPrincipal CustomUserDetails customUserDetails, @PathVariable("illness") String illness) {
        Optional<List<Doctor>> doctors = doctorService.getByIllness(illness);

        if (doctors.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        return ResponseEntity.status(HttpStatus.OK).body(doctors.get().stream().map(DoctorMapper.INSTANCE::convert).toList());
    }

    @RequiredPrivilege(value = Privilege.CHECK_ASSIGNED_ILLNESSES)
    @GetMapping("/my-illnesses")
    public ResponseEntity<Set<IllnessResponse>> checkAssignedIllnesses(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        Optional<Set<Illness>> illnesses = illnessService.checkAssignedIllnesses(customUserDetails.getUsername());

        if (illnesses.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

        return ResponseEntity.status(HttpStatus.OK).body(illnesses.get().stream().map(IllnessMapper.INSTANCE::convert).collect(Collectors.toSet()));
    }

    @RequiredPrivilege(value = Privilege.CHECK_PRESCRIPTIONS)
    @GetMapping("/my-prescriptions")
    public ResponseEntity<List<PrescriptionResponse>> checkAssignedPrescriptions(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        List<Prescription> prescriptions = prescriptionService.checkPrescriptions(customUserDetails.getUsername());

        if (prescriptions.isEmpty())
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

        return ResponseEntity.status(HttpStatus.OK).body(prescriptions.stream().map(PrescriptionMapper.INSTANCE::convert).toList());
    }

    @RequiredPrivilege(value = Privilege.ASK_QUESTION)
    @PostMapping("/ask-question")
    public ResponseEntity<Response> askQuestion(@AuthenticationPrincipal CustomUserDetails customUserDetails, @RequestBody QuestionRequest questionRequest) {
        Response response = questionService.askQuestion(questionRequest, customUserDetails.getUsername());
        return ResponseEntity.status(response.httpStatus()).body(response);
    }

    @RequiredPrivilege(value = Privilege.ASK_QUESTION)
    @GetMapping("/questions")
    public ResponseEntity<List<QuestionResponse>> checkAskedQuestions(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
         Optional<List<Question>> questions = questionService.getAskedQuestions(customUserDetails.getUsername());

         if (questions.isEmpty())
             return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

         return ResponseEntity.status(HttpStatus.OK).body(questions.get().stream().map(QuestionMapper.INSTANCE::convert).toList());
    }

    @RequiredPrivilege(value = Privilege.ASK_QUESTION)
    @GetMapping("/answers")
    public ResponseEntity<List<Answer>> checkAnswers(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        Optional<List<Answer>> answers = answerService.getAnswers(customUserDetails.getUsername());

        if (answers.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

        return ResponseEntity.status(HttpStatus.OK).body(answers.get());
    }

}

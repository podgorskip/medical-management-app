package app.registrationSystem.controllers;

import app.registrationSystem.dto.request.AnswerRequest;
import app.registrationSystem.dto.response.Response;
import app.registrationSystem.jpa.entities.Answer;
import app.registrationSystem.security.CustomUserDetails;
import app.registrationSystem.security.Privilege;
import app.registrationSystem.security.RequiredPrivilege;
import app.registrationSystem.services.AnswerService;
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
public class AnswerController {
    private final AnswerService answerService;

    @RequiredPrivilege(value = Privilege.ANSWER_QUESTION)
    @PostMapping("/doctor/answer-question")
    public ResponseEntity<Response> answerQuestion(@AuthenticationPrincipal CustomUserDetails customUserDetails, @RequestParam("id") Long id, @RequestBody AnswerRequest answerRequest) {
        Response response = answerService.answerQuestion(id, customUserDetails.getUsername(), answerRequest);
        return ResponseEntity.status(response.httpStatus()).body(response);
    }

    @RequiredPrivilege(value = Privilege.ASK_QUESTION)
    @GetMapping("/patient/answers")
    public ResponseEntity<List<Answer>> checkAnswers(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        Optional<List<Answer>> answers = answerService.getAnswers(customUserDetails.getUsername());

        if (answers.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

        return ResponseEntity.status(HttpStatus.OK).body(answers.get());
    }
}

package app.registrationSystem.controllers;

import app.registrationSystem.dto.mappers.QuestionMapper;
import app.registrationSystem.dto.request.QuestionRequest;
import app.registrationSystem.dto.response.QuestionResponse;
import app.registrationSystem.dto.response.Response;
import app.registrationSystem.jpa.entities.Question;
import app.registrationSystem.security.CustomUserDetails;
import app.registrationSystem.security.Privilege;
import app.registrationSystem.security.RequiredPrivilege;
import app.registrationSystem.services.QuestionService;
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
public class QuestionController {
    private final QuestionService questionService;

    @RequiredPrivilege(value = Privilege.ANSWER_QUESTION)
    @GetMapping("/doctor/questions")
    public ResponseEntity<List<QuestionResponse>> checkUnansweredQuestions(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        Optional<List<Question>> questions = questionService.getQuestionsToAnswer(customUserDetails.getUsername());

        if (questions.isEmpty()) return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

        return ResponseEntity.status(HttpStatus.OK).body(questions.get().stream().map(QuestionMapper.INSTANCE::convert).toList());
    }

    @RequiredPrivilege(value = Privilege.ASK_QUESTION)
    @PostMapping("/patient/ask-question")
    public ResponseEntity<Response> askQuestion(@AuthenticationPrincipal CustomUserDetails customUserDetails, @RequestBody QuestionRequest questionRequest) {
        Response response = questionService.askQuestion(questionRequest, customUserDetails.getUsername());
        return ResponseEntity.status(response.httpStatus()).body(response);
    }

    @RequiredPrivilege(value = Privilege.ASK_QUESTION)
    @GetMapping("/patient/questions")
    public ResponseEntity<List<QuestionResponse>> checkAskedQuestions(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        Optional<List<Question>> questions = questionService.getAskedQuestions(customUserDetails.getUsername());

        if (questions.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

        return ResponseEntity.status(HttpStatus.OK).body(questions.get().stream().map(QuestionMapper.INSTANCE::convert).toList());
    }
}

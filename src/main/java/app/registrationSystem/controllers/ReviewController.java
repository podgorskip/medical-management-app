package app.registrationSystem.controllers;

import app.registrationSystem.jpa.entities.VisitHistory;
import app.registrationSystem.security.CustomUserDetails;
import app.registrationSystem.security.Privilege;
import app.registrationSystem.security.RequiredPrivilege;
import app.registrationSystem.services.VisitService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ReviewController {
    private final VisitService visitService;

    @RequiredPrivilege(value = Privilege.REVIEW_VISIT)
    @GetMapping("/to-review")
    public ResponseEntity<List<VisitHistory>> checkVisitsToReview(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        Optional<List<VisitHistory>> visitHistories = visitService.getVisitsWaitingForReview(customUserDetails.getUsername());

        if (visitHistories.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

        return ResponseEntity.status(HttpStatus.OK).body(visitHistories.get());
    }
}

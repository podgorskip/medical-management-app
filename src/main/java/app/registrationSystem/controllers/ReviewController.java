package app.registrationSystem.controllers;

import app.registrationSystem.dto.mappers.ReviewMapper;
import app.registrationSystem.dto.mappers.VisitHistoryMapper;
import app.registrationSystem.dto.request.ReviewRequest;
import app.registrationSystem.dto.response.Response;
import app.registrationSystem.dto.response.ReviewResponse;
import app.registrationSystem.dto.response.VisitHistoryResponse;
import app.registrationSystem.jpa.entities.Review;
import app.registrationSystem.jpa.entities.VisitHistory;
import app.registrationSystem.security.CustomUserDetails;
import app.registrationSystem.security.Privilege;
import app.registrationSystem.security.RequiredPrivilege;
import app.registrationSystem.services.ReviewService;
import app.registrationSystem.services.VisitService;
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
public class ReviewController {
    private final VisitService visitService;
    private final ReviewService reviewService;

    @RequiredPrivilege(value = Privilege.REVIEW_VISIT)
    @GetMapping("/patient/to-review")
    public ResponseEntity<List<VisitHistoryResponse>> checkVisitsToReview(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        Optional<List<VisitHistory>> visitHistories = visitService.getVisitsWaitingForReview(customUserDetails.getUsername());

        return visitHistories
                .map(histories -> ResponseEntity.status(HttpStatus.OK).body(histories.stream().map(VisitHistoryMapper.INSTANCE::convert).toList()))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());

    }

    @RequiredPrivilege(value = Privilege.REVIEW_VISIT)
    @PostMapping("/patient/review")
    public ResponseEntity<Response> reviewVisit(@AuthenticationPrincipal CustomUserDetails customUserDetails, @RequestParam("id") Long id, @RequestBody ReviewRequest reviewRequest) {
        Response response = reviewService.reviewVisit(id, customUserDetails.getUsername(), reviewRequest);
        return ResponseEntity.status(response.httpStatus()).body(response);
    }

    @GetMapping("/auth/reviews")
    public ResponseEntity<List<ReviewResponse>> checkReviews(@AuthenticationPrincipal CustomUserDetails customUserDetails, @RequestParam("id") Long id) {
        Optional<List<Review>> reviews = reviewService.getByDoctor(id);

        return reviews
                .map(reviewList -> ResponseEntity.status(HttpStatus.OK).body(reviewList.stream().map(ReviewMapper.INSTANCE::convert).toList()))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }
}

package app.registrationSystem.services;

import app.registrationSystem.dto.request.ReviewRequest;
import app.registrationSystem.dto.response.Response;
import app.registrationSystem.jpa.entities.Patient;
import app.registrationSystem.jpa.entities.Review;
import app.registrationSystem.jpa.entities.VisitHistory;
import app.registrationSystem.jpa.repositories.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * A class that handles operations related to Review entity
 */
@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final PatientService patientService;
    private final VisitService visitService;

    /**
     * Allows to write a review on a visit
     * @param id ID of the visit to review
     * @param username username of the patient reviewing the visit
     * @param reviewRequest comment and rating
     * @return response with status of the performed action
     */
    public Response reviewVisit(Long id, String username, ReviewRequest reviewRequest) {
        Optional<Patient> patient = patientService.getByUsername(username);

        if (patient.isEmpty())
            return new Response(false, HttpStatus.NOT_FOUND, "No patient of the provided username found");

        Optional<VisitHistory> visitHistory = visitService.getVisitHistoryByID(id);

        if (visitHistory.isEmpty())
            return new Response(false, HttpStatus.NOT_FOUND, "No visit history of the ID found");

        reviewRepository.save(createReview(patient.get(), visitHistory.get(), reviewRequest));

        return new Response(true, HttpStatus.CREATED, "Correctly reviewed the visit");
    }

    /**
     * Returns list of reviews by a doctor
     * @param id ID of the doctor
     * @return list of reviews is available, empty otherwise
     */
    public Optional<List<Review>> getByDoctor(Long id) {
        Optional<List<VisitHistory>> visitHistories = visitService.getVisitHistoriesByDoctorID(id);

        return visitHistories.map(histories -> histories.stream().map(reviewRepository::findByVisitHistory).filter(Optional::isPresent).map(Optional::get).collect(Collectors.toList()));
    }

    /**
     * Creates a review instance
     * @param patient patient who reviews the visit
     * @param visitHistory visit details
     * @param reviewRequest comment and rating
     * @return Review instance
     */
    private Review createReview(Patient patient, VisitHistory visitHistory, ReviewRequest reviewRequest) {
        Review review = new Review();

        review.setVisitHistory(visitHistory);
        review.setComment(reviewRequest.getComment());
        review.setRating(reviewRequest.getRating());
        review.setDate(LocalDateTime.now());

        return review;
    }
}

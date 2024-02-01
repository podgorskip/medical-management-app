package app.registrationSystem.dto.mappers;

import app.registrationSystem.dto.response.ReviewResponse;
import app.registrationSystem.jpa.entities.Review;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ReviewMapper {
    ReviewMapper INSTANCE = Mappers.getMapper(ReviewMapper.class);

    default ReviewResponse convert(Review review) {
        ReviewResponse reviewResponse = new ReviewResponse();

        reviewResponse.setPatient(review.getVisitHistory().getPatient().getUser().fullName());
        reviewResponse.setDoctor(review.getVisitHistory().getDoctor().getUser().fullName());
        reviewResponse.setComment(review.getComment());
        reviewResponse.setVisitDate(review.getVisitHistory().getDate());
        reviewResponse.setReviewDate(review.getDate());
        reviewResponse.setRating(review.getRating());

        return reviewResponse;
    }
}

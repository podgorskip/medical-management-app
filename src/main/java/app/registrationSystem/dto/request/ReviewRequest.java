package app.registrationSystem.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class ReviewRequest {
    @Min(value = 1, message = "Rating must be at least 1")
    @Max(value = 10, message = "Rating cannot be greater than 10")
    private int rating;

    private String comment;
}

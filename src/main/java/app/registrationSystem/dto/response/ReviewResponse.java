package app.registrationSystem.dto.response;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ReviewResponse {
    private String patient;
    private String doctor;
    private int rating;
    private String comment;
    private LocalDateTime visitDate;
    private LocalDateTime reviewDate;
}

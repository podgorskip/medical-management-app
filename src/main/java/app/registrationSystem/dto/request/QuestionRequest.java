package app.registrationSystem.dto.request;

import lombok.Data;
import lombok.NonNull;

@Data
public class QuestionRequest {
    @NonNull
    private Long specializationID;

    @NonNull
    private String question;
}

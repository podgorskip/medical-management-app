package app.registrationSystem.dto.response;

import lombok.Data;

@Data
public class QuestionResponse {
    private Long id;
    private String patient;
    private String specialization;
    private String question;
    private String date;
}

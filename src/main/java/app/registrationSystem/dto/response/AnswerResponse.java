package app.registrationSystem.dto.response;

import lombok.Data;

@Data
public class AnswerResponse {
    private String question;
    private String answer;
    private String doctor;
    private String specialization;
    private String date;
}

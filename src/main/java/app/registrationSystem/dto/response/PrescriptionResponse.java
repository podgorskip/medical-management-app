package app.registrationSystem.dto.response;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class PrescriptionResponse {
    private String patient;
    private List<String> medications;
    private String code;
    private String description;
    private LocalDateTime issueDate;
    private LocalDateTime expirationDate;
    private String doctor;
}

package app.registrationSystem.dto.response;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class VisitHistoryResponse {
    private Long id;
    private String patient;
    private String doctor;
    private LocalDateTime date;
    private String specialization;
}

package app.registrationSystem.dto.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ScheduledVisitResponse {
    private Long id;
    private String patientFirstName;
    private String patientLastName;
    private String patientEmail;
    private String doctorFirstName;
    private String doctorLastName;
    private String doctorEmail;
    private int duration;
    private LocalDateTime date;
    private String specialization;
}

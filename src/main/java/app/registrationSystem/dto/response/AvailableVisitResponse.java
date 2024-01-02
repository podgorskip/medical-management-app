package app.registrationSystem.dto.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AvailableVisitResponse {
    private Long id;
    private String doctorFirstName;
    private String doctorLastName;
    private String doctorEmail;
    private int duration;
    private LocalDateTime date;
    private String specialization;
}

package app.registrationSystem.dto.response;

import lombok.Data;

@Data
public class DoctorResponse {
    private String firstName;
    private String lastName;
    private String email;
    private String specialization;
}

package app.registrationSystem.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class DoctorRegistrationRequest extends UserRegistrationRequest {
    private Long specialization;
}

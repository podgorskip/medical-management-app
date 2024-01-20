package app.registrationSystem.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import java.util.Set;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PatientRegistrationRequest extends UserRegistrationRequest {
    private Set<Long> illnesses;
}

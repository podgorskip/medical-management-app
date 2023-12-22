package app.registrationSystem.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import java.util.Set;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PatientDTO extends UserDTO {
    private Set<Long> illnesses;
}

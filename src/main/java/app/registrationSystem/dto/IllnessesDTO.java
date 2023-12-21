package app.registrationSystem.dto;

import lombok.Data;

import java.util.Set;

@Data
public class IllnessesDTO {
    private Set<Long> illnesses;
}

package app.registrationSystem.dto.request;

import lombok.Data;
import java.util.Set;

@Data
public class IllnessesRequest {
    private Set<Long> illnesses;
}

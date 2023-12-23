package app.registrationSystem.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
public class VisitsDTO {
    private List<VisitDTO> visits;
}

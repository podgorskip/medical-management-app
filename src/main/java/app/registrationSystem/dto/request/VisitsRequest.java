package app.registrationSystem.dto.request;

import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
public class VisitsRequest {
    private List<VisitRequest> visits;
}

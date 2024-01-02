package app.registrationSystem.dto.request;

import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class VisitRequest {
    private LocalDateTime date;
    private int duration;
}

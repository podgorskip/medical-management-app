package app.registrationSystem.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class VisitDTO {
    private LocalDateTime date;
    private int duration;
}

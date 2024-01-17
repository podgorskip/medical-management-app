package app.registrationSystem.dto.request;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class VisitRequest {
    @NonNull
    private LocalDateTime date;

    @NonNull
    private Integer duration;
}

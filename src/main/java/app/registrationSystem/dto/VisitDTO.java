package app.registrationSystem.dto;

import lombok.Data;
import java.util.Date;
import java.util.List;

@Data
public class VisitsDTO {
    private List<Date> dates;
}

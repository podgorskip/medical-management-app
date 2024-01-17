package app.registrationSystem.dto.response;

import lombok.Data;
import java.util.List;

@Data
public class MedicationResponse {
    private Long id;
    private String name;
    private int dose;
    private String description;
    private List<String> specializations;
}

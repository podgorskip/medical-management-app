package app.registrationSystem.dto.request;

import lombok.Data;
import lombok.NonNull;
import java.util.List;

@Data
public class PrescriptionRequest {
    @NonNull
    private Long patientID;

    @NonNull
    private List<Long> medicationsID;

    @NonNull
    private String description;
}

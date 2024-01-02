package app.registrationSystem.dto.response;

import lombok.Data;

@Data
public class IllnessResponse {
    private Long id;
    private String name;
    private int severity;
    private String specialization;
    private int visitDuration;
}

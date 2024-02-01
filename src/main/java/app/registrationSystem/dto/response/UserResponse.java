package app.registrationSystem.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserResponse {
    private String fullName;
    private String email;
    private String phoneNumber;
}

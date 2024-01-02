package app.registrationSystem.dto.request;


import jakarta.validation.constraints.NotBlank;

public record PasswordChangeRequest(@NotBlank String oldPassword, @NotBlank String newPassword) {
}

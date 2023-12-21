package app.registrationSystem.dto;

import org.apache.logging.log4j.core.config.plugins.validation.constraints.NotBlank;

public record PasswordChangeRequest(@NotBlank String oldPassword,@NotBlank String newPassword) {
}

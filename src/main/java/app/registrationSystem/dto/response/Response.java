package app.registrationSystem.dto.response;

import org.springframework.http.HttpStatus;

public record Response(boolean success, HttpStatus httpStatus, String message) {
}

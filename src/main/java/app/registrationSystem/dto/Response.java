package app.registrationSystem.dto;

import org.springframework.http.HttpStatus;

public record Response(boolean success, HttpStatus httpStatus, String message) {
}

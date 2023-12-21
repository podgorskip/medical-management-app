package app.registrationSystem.exceptions;

import io.micrometer.common.lang.NonNullApi;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Data
public class ErrorResponse {
    private final HttpStatus httpStatus;
    private final String details;
}

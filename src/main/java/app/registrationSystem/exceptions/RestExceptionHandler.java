package app.registrationSystem.exceptions;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class RestExceptionHandler {
    private static final Logger log = LogManager.getLogger(RestExceptionHandler.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> handleConstraintViolationException(MethodArgumentNotValidException e) {

        String message = "Invalid due to validation error related to the parameter: " + e.getParameter();
        log.warn(message);

        return new ResponseEntity<>(new ErrorResponse(HttpStatus.BAD_REQUEST, message), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UnauthorizedException.class)
    @ResponseStatus(value = HttpStatus.FORBIDDEN)
    public ResponseEntity<ErrorResponse> handleUnauthorizedException(UnauthorizedException e) {

        String message = "Invalid due to validation error: " + e.getMessage();
        log.warn(message);

        return new ResponseEntity<>(new ErrorResponse(HttpStatus.FORBIDDEN, message), HttpStatus.FORBIDDEN);
    }

}

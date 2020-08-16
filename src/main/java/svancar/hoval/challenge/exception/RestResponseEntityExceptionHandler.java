package svancar.hoval.challenge.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import svancar.hoval.challenge.exception.error.ApiError;
import svancar.hoval.challenge.exception.exceptions.FileValidationException;
import svancar.hoval.challenge.exception.exceptions.NotFoundException;
import svancar.hoval.challenge.exception.exceptions.ValidationException;

import java.util.HashMap;
import java.util.Map;

/**
 * Class handles all below mentioned exceptions if they are thrown in the application
 */
@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    private final Logger logger = LoggerFactory.getLogger(RestResponseEntityExceptionHandler.class);


    @ExceptionHandler(value = { ValidationException.class })
    public ResponseEntity<Object> handleValidationException(
            ValidationException ex, WebRequest request) {

        Map<String, String> errors = new HashMap<>();
        for (ObjectError error : ex.getErrors()) {
            FieldError fieldError = (FieldError) error;
            errors.put(fieldError.getField(), fieldError.getDefaultMessage());
        }
        logger.error("Validation exception. Error fields: {}", errors);
        ApiError apiError =
                new ApiError(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), errors);
        return new ResponseEntity<>(
                apiError, new HttpHeaders(), apiError.getStatusCode());
    }

    @ExceptionHandler(NotFoundException.class)
    public final ResponseEntity<Object> handleNotFoundException(NotFoundException ex, WebRequest request) {
        logger.error(ex.getLocalizedMessage());
        Map<String, String> errors = new HashMap<>();
        ApiError apiError =
                new ApiError(HttpStatus.NOT_FOUND, ex.getLocalizedMessage(), errors);
        return new ResponseEntity<>(
                apiError, new HttpHeaders(), apiError.getStatusCode());
    }

    @ExceptionHandler(FileValidationException.class)
    public final ResponseEntity<Object> handleFileValidationException(FileValidationException ex, WebRequest request) {
        logger.error(ex.getLocalizedMessage());
        Map<String, String> errors = new HashMap<>();
        errors.put("file", ex.getLocalizedMessage());
        ApiError apiError =
                new ApiError(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), errors);
        return new ResponseEntity<>(
                apiError, new HttpHeaders(), apiError.getStatusCode());
    }
}

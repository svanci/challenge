package svancar.hoval.challenge.exception.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.validation.ObjectError;

import java.util.List;

public class ValidationException extends RuntimeException {

    private HttpStatus httpStatus;
    private List<ObjectError> errors;

    public ValidationException() {
        super();
    }

    public ValidationException(HttpStatus statusCode, String message, List<ObjectError> errors) {
        super(message);
        this.httpStatus = statusCode;
        this.errors = errors;
    }

    public ValidationException(String message, Throwable cause) {
        super(message, cause);
    }

    public ValidationException(Throwable cause) {
        super(cause);
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public List<ObjectError> getErrors() {
        return errors;
    }
}

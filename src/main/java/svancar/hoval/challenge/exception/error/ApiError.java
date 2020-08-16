package svancar.hoval.challenge.exception.error;

import org.springframework.http.HttpStatus;

import java.util.Map;

/**
 * Class for handling custom error response
 */
public class ApiError {

    private HttpStatus statusCode;
    private String message;
    private Map<String, String> errors;

    public ApiError(HttpStatus statusCode, String message, Map<String, String> errors) {
        this.statusCode = statusCode;
        this.message = message;
        this.errors = errors;
    }

    public HttpStatus getStatusCode() {
        return statusCode;
    }

    public String getMessage() {
        return message;
    }

    public Map<String, String> getErrors() {
        return errors;
    }
}

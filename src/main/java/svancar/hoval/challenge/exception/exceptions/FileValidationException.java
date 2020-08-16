package svancar.hoval.challenge.exception.exceptions;

public class FileValidationException extends RuntimeException {

    public FileValidationException() {
        super();
    }

    public FileValidationException(String message) {
        super(message);
    }

    public FileValidationException(String message, Throwable cause) {
        super(message, cause);
    }

    public FileValidationException(Throwable cause) {
        super(cause);
    }
}

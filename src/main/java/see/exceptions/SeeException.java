package see.exceptions;

/**
 * Base class for exception hierarchy
 */
public abstract class SeeException extends RuntimeException {
    protected SeeException() {
    }

    protected SeeException(String message) {
        super(message);
    }

    protected SeeException(String message, Throwable cause) {
        super(message, cause);
    }

    protected SeeException(Throwable cause) {
        super(cause);
    }
}

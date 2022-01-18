package infrastructure.exception;

/**
 * Exception for signal about reflation work exception
 *
 * @author Vendelovskyi Ivan
 * @version 1.0
 */
public class ReflectionException extends RuntimeException {
    public ReflectionException(String message) {
        super(message);
    }

    public ReflectionException() {
    }
}

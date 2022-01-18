package infrastructure.exception;

/**
 * Exception for signal about problems in configuration
 *
 * @author Vendelovskyi Ivan
 * @version 1.0
 */
public class ConfigurationException extends RuntimeException {
    public ConfigurationException(String message) {
        super(message);
    }
}

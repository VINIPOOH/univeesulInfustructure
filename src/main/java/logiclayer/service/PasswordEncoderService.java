package logiclayer.service;

/**
 * Declares an interface for password encode services
 *
 * @author Vendelovskyi Ivan
 * @version 1.0
 */
public interface PasswordEncoderService {
    String encode(String password);
}

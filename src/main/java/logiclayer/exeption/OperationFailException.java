package logiclayer.exeption;

/**
 * Used when there no other exception but we need to
 * throw some exception to tell transaction manager then transaction need rollback
 *
 * @author Vendelovskyi Ivan
 * @version 1.0
 */
public class OperationFailException extends Exception {
}

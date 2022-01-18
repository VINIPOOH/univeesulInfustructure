package infrastructure.anotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author Vendelovskyi Ivan
 * @version 1.0
 */
@Target(ElementType.METHOD)
@Retention(RUNTIME)
public @interface Transaction {
}

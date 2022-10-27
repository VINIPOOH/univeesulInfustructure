package infrastructure.anotation.rest;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author Vendelovskyi Ivan
 * @version 1.0
 * Puts on method which updates entity by id
 */
@Target(ElementType.METHOD)
@Retention(RUNTIME)
public @interface RestUpdate {
}

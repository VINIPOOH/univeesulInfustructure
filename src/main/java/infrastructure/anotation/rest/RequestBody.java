package infrastructure.anotation.rest;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author Vendelovskyi Ivan
 * @version 1.0
 */
//todo ivan add support of this annotation for httpEndpoint
@Inherited
@Target(ElementType.PARAMETER)
@Retention(RUNTIME)
public @interface RequestBody {
}

package infrastructure.anotation.rest;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Inherited
@Target(ElementType.PARAMETER)
@Retention(RUNTIME)
public @interface RequestParam {
    String paramName();
}

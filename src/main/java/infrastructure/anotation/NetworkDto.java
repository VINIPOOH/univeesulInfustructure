package infrastructure.anotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Inherited
@Target(ElementType.TYPE)
@Retention(RUNTIME)
public @interface NetworkDto {
    String massageCode();
}

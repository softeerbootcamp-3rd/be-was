package webserver;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ExceptionHandler {
  Class<? extends RuntimeException> value() default RuntimeException.class;
}
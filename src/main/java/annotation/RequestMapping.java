package annotation;

import http.RequestMethod;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface RequestMapping {

    /**
     * 요청을 처리할 경로를 지정합니다.
     */
    String value() default "";

    /**
     * HTTP 메서드를 지정합니다. 기본값은 GET입니다.
     */
    RequestMethod method() default RequestMethod.GET;

    /**
     * 요청을 처리할 미디어 타입을 지정합니다.
     */
    String[] produces() default {};

    /**
     * 요청의 헤더를 검사하여 지정된 헤더가 일치할 때만 메서드를 수행합니다.
     */
    String[] headers() default {};

    /**
     * 요청의 헤더에 지정된 헤더가 없을 때만 메서드를 수행합니다.
     */
    String[] headersNotPresent() default {};

    /**
     * 요청의 특정 파라미터가 있는 경우에만 메서드를 수행합니다.
     */
    String[] params() default {};

    /**
     * 요청의 특정 파라미터가 없는 경우에만 메서드를 수행합니다.
     */
    String[] paramsNotPresent() default {};
}

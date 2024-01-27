package http;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

import static http.MethodMapper.getMethod;

class MethodMapperTest {

    @Test
    @DisplayName("end point에 맞는 method 반환")
    void getMethodTest() {

        //given
        String endPoint = "POST /user/create";

        //when
        Method method = getMethod(endPoint);

        //then
        Assertions.assertThat(method.getName()).isEqualTo("create");
    }
}

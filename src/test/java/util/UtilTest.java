package util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class UtilTest {

    @Test
    @DisplayName("Request line을 http method, url, version으로 분리")
    void splitRequestLine() {

        //given
        String requestLine = "GET /index.html HTTP/1.1";

        //when
        String[] result = Util.splitRequestLine(requestLine);

        //then
        assertThat(result).contains("GET", "/index.html", "HTTP/1.1");
    }

    @Test
    @DisplayName("Request header를 속성(키)-값으로 분리")
    void splitRequestHeader() {

        //given
        String requestHeader = "Host: localhost:8080";

        //when
        String[] result = Util.splitRequestHeader(requestHeader);

        //then
        assertThat(result[0]).isEqualTo("Host");
        assertThat(result[1]).isEqualTo("localhost:8080");
    }

    @Test
    @DisplayName("url을 경로와 파라미터 문자열로 분리")
    void splitUrlToPathAndParameters() {

        //given
        String url = "/create?userId=javajigi&password=password&name=%EB%B0%95%EC%9E%AC%EC%84%B1&email=javajigi%40slipp.net";

        //when
        String[] result = Util.splitUrlToPathAndParameters(url);

        //then
        assertThat(result.length).isEqualTo(2);
        assertThat(result[0]).isEqualTo("/create");
        assertThat(result[1]).isEqualTo("userId=javajigi&password=password&name=%EB%B0%95%EC%9E%AC%EC%84%B1&email=javajigi%40slipp.net");
    }

    @Test
    void splitParamtersToKeyAndValue() {

        //given
        String parameters = "userId=javajigi&password=password&name=%EB%B0%95%EC%9E%AC%EC%84%B1&email=javajigi%40slipp.net";

        //when
        Map<String, String> result = Util.splitParamtersToKeyAndValue(parameters);

        //then
        assertThat(result.keySet()).contains("userId", "password", "name", "email");
        assertThat(result.get("userId")).isEqualTo("javajigi");
        assertThat(result.get("password")).isEqualTo("password");
    }
}
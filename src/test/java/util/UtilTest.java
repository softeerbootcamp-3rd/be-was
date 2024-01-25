package util;

import common.util.Util;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.UnsupportedEncodingException;

import static common.util.Util.*;
import static org.assertj.core.api.Assertions.assertThat;
import static webserver.RequestParser.*;

class UtilTest {

    @Test
    @DisplayName("Request line을 http method, url, version으로 분리")
    void splitRequestLine() {

        //given
        String requestLine = "GET /index.html HTTP/1.1";

        //when
        String[] result = split(requestLine, REQUEST_LINE_DELIMITER);

        //then
        assertThat(result).contains("GET", "/index.html", "HTTP/1.1");
    }

    @Test
    @DisplayName("Request header를 속성(키)-값으로 분리")
    void splitRequestHeader() {

        //given
        String requestHeader = "Host: localhost:8080";

        //when
        String[] result = split(requestHeader, REQUEST_HEADER_DELIMITER);

        //then
        assertThat(result[0]).isEqualTo("Host");
        assertThat(result[1]).isEqualTo("localhost:8080");
    }

    @Test
    @DisplayName("url을 경로와 쿼리스트링 문자열로 분리")
    void splitUrlToPathAndQueryString() {

        //given
        String url = "/create?userId=javajigi&password=password&name=%EB%B0%95%EC%9E%AC%EC%84%B1&email=javajigi%40slipp.net";

        //when
        String[] result = split(url, URL_DELIMITER);

        //then
        assertThat(result.length).isEqualTo(2);
        assertThat(result[0]).isEqualTo("/create");
        assertThat(result[1]).isEqualTo("userId=javajigi&password=password&name=%EB%B0%95%EC%9E%AC%EC%84%B1&email=javajigi%40slipp.net");
    }

    @Test
    @DisplayName("인코딩된 문자열의 디코딩 테스트")
    void decode() throws UnsupportedEncodingException {

        //given
        String encoded = "%EB%B0%95%EC%9E%AC%EC%84%B1";

        //when
        String result = Util.decode(encoded);

        //then
        assertThat(result).isEqualTo("박재성");
    }
}

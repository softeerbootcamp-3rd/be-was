package util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static common.util.Util.*;
import static org.assertj.core.api.Assertions.assertThat;

class UtilTest {

    @Test
    @DisplayName("Request line을 http method, url, version으로 분리")
    void splitRequestLine() {

        //given
        String requestLine = "GET /index.html HTTP/1.1";

        //when
        String[] result = split(requestLine, " ");

        //then
        assertThat(result).contains("GET", "/index.html", "HTTP/1.1");
    }

    @Test
    @DisplayName("10자리 랜덤 문자열 생성")
    void getSessionIdTest() {

        //when
        String sessionId = getSessionId();

        //then
        assertThat(sessionId.length()).isEqualTo(10);
        assertThat(sessionId.getClass()).isEqualTo(String.class);
    }
}

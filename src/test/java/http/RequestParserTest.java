package http;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static http.RequestParser.parseMethod;
import static http.RequestParser.parseUrl;
import static org.assertj.core.api.Assertions.*;

class RequestParserTest {

    @Test
    @DisplayName("request line에서 method를 파싱")
    void parseMethodTest() {

        //given
        String requestLine = "POST /user/create?userId=javajigi HTTP/1.1";

        //when
        String result = parseMethod(requestLine);

        //then
        assertThat(result).isEqualTo("POST");

    }

    @Test
    @DisplayName("request line에서 path와 query string을 파싱 - query string이 있는 경우")
    void parseUrlTest1() {
        //given
        String requestLine = "POST /user/create?userId=javajigi HTTP/1.1";

        //when
        String[] result = parseUrl(requestLine);

        //then
        assertThat(result.length).isEqualTo(2);
        assertThat(result[0]).isEqualTo("/user/create");
        assertThat(result[1]).isEqualTo("userId=javajigi");
    }

    @Test
    @DisplayName("request line에서 path와 query string을 파싱 - query string이 없는 경우")
    void parseUrlTest2() {
        //given
        String requestLine = "GET /user/1 HTTP/1.1";

        //when
        String[] result = parseUrl(requestLine);

        //then
        assertThat(result.length).isEqualTo(2);
        assertThat(result[0]).isEqualTo("/user/1");
        assertThat(result[1]).isEqualTo(null);
    }
}

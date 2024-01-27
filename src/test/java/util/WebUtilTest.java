package util;

import dto.HttpRequestDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class WebUtilTest {

    @Test
    @DisplayName("httpRequestParser(): HTTP Request 요청을 HttpRequestDto로 적절하게 파싱한다.")
    public void httpRequestParseTest() {
        // given
        String testRequest = "GET /index.html HTTP/1.1\n" +
                "Host: localhost:8080\n" +
                "Connection: keep-alive\n" +
                "Accept: */*\n" +
                "Content-Length: 12\n\n" +
                "request body";
        InputStream inputStream = new ByteArrayInputStream(testRequest.getBytes());

        // when
        HttpRequestDto parsedRequest = WebUtil.httpRequestParse(inputStream);

        // then
        Assertions.assertThat(parsedRequest.getMethod()).isEqualTo("GET");
        Assertions.assertThat(parsedRequest.getUri()).isEqualTo("/index.html");
        Assertions.assertThat(parsedRequest.getHttpVersion()).isEqualTo("HTTP/1.1");
        Assertions.assertThat(parsedRequest.getHeaders().get("Host")).isEqualTo("localhost:8080");
        Assertions.assertThat(parsedRequest.getHeaders().get("Connection")).isEqualTo("keep-alive");
        Assertions.assertThat(parsedRequest.getHeaders().get("Accept")).isEqualTo("*/*");
        Assertions.assertThat(parsedRequest.getBody()).isEqualTo("request body");
        Assertions.assertThat(parsedRequest.getUser()).isNull();

    }

    @Test()
    @DisplayName("getContentType(): URI를 인자로 받아서 알맞은 contentType을 반환한다.")
    public void getContentTypeTest() {
        // given
        String testUri = "/index.html";

        // when
        String contentType = WebUtil.getContentType(testUri);

        // then
        Assertions.assertThat(contentType).isEqualTo("text/html");
    }

    @Test()
    @DisplayName("getPath(): URI를 인자로 받아 요청 리소스의 위치를 반환한다.")
    public void getPathTest() {
        // given
        String testUri = "/index.html";
        String testUri2 = "/css/bootstrap.min.css";

        // when
        String path = WebUtil.getPath(testUri);
        String path2 = WebUtil.getPath(testUri2);

        // then
        Assertions.assertThat(path).isEqualTo("./src/main/resources/templates" + testUri);
        Assertions.assertThat(path2).isEqualTo("./src/main/resources/static" + testUri2);
    }

    @Test()
    @DisplayName("parseQueryString(): URI에서 쿼리 스트링을 파싱하여 Map 형태로 반환한다.")
    public void parseQueryStringTest() {
        // given
        String uri = "/user/create?userId=userId&password=password&name=testName&email=softeer@example.com";
        String encodedUri = URLEncoder.encode(uri, StandardCharsets.UTF_8);

        // when
        Map<String, String> parsedUri = WebUtil.parseQueryString(encodedUri);

        // then
        Assertions.assertThat(parsedUri.get("userId")).isEqualTo("userId");
        Assertions.assertThat(parsedUri.get("password")).isEqualTo("password");
        Assertions.assertThat(parsedUri.get("name")).isEqualTo("testName");
        Assertions.assertThat(parsedUri.get("email")).isEqualTo("softeer@example.com");
    }

    @Test()
    @DisplayName("parseRequestBody(): Body를 파싱하여 Map 형태로 반환한다.")
    public void parseRequestBodyTest() {
        // given
        String body = "userId=1&password=hello&name=suji&email=example%40softeer.com";

        // when
        Map<String, String> parsedBody = WebUtil.parseRequestBody(body);

        // then
        Assertions.assertThat(parsedBody.get("userId")).isEqualTo("1");
        Assertions.assertThat(parsedBody.get("password")).isEqualTo("hello");
        Assertions.assertThat(parsedBody.get("name")).isEqualTo("suji");
        Assertions.assertThat(parsedBody.get("email")).isEqualTo("example@softeer.com");
    }

    @Test
    @DisplayName("parseCookie(): 헤더의 쿠키 값을 파싱하여 Map 형태로 반환한다.")
    public void parseCookieTest() {
        // given
        String cookie = "Idea-69015365=809ed775-1433-48aa-bb94-f81d467909f8; sid=88ef7daf-c0ef-4723-8b96-04de53f5aab3";

        // when
        Map<String, String> parsedCookie = WebUtil.parseCookie(cookie);

        // then
        Assertions.assertThat(parsedCookie.get("sid")).isEqualTo("88ef7daf-c0ef-4723-8b96-04de53f5aab3");
    }
}

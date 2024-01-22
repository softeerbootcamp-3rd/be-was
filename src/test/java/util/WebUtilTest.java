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
    @DisplayName("WebUtil.httpRequestParser() Test")
    public void httpRequestParseTest() {
        // given
        String mockRequest = "GET /index.html HTTP/1.1\n" +
                "Host: localhost:8080\n" +
                "Connection: keep-alive\n" +
                "Accept: */*\n" +
                "Content-Length: 12\n\n" +
                "request body";
        InputStream inputStream = new ByteArrayInputStream(mockRequest.getBytes());

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

    }

    @Test()
    @DisplayName("WebUtil.getContentType() Test")
    public void getContentTypeTest() {
        // given
        String mockUri = "/index.html";

        // when
        String contentType = WebUtil.getContentType(mockUri);

        // then
        Assertions.assertThat(contentType).isEqualTo("text/html");
    }

    @Test()
    @DisplayName("WebUtil.getPath() Test")
    public void getPathTest() {
        // given
        String mockUri = "/index.html";
        String mockUri2 = "/css/bootstrap.min.css";

        // when
        String path = WebUtil.getPath(mockUri);
        String path2 = WebUtil.getPath(mockUri2);

        // then
        Assertions.assertThat(path).isEqualTo("./src/main/resources/templates" + mockUri);
        Assertions.assertThat(path2).isEqualTo("./src/main/resources/static" + mockUri2);
    }

    @Test()
    @DisplayName("WebUtil.parseQueryString() Test")
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
    @DisplayName("WebUtil.parseRequestBody() Test")
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
}

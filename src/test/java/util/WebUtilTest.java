package util;

import dto.HttpRequestDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import util.WebUtil;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

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
}

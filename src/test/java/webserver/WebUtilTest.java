package webserver;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class WebUtilTest {
    private static final Logger logger = LoggerFactory.getLogger(WebServer.class);

    @Test
    @DisplayName("HttpRequestParser Test")
    public void HttpRequestParseTest() {
        // given
        String mockRequest = "GET /index.html HTTP/1.1\n" +
                "Host: localhost:8080\n" +
                "Connection: keep-alive\n" +
                "Accept: */*";
        InputStream inputStream = new ByteArrayInputStream(mockRequest.getBytes());

        // when
        HttpRequestParam parsedRequest = WebUtil.HttpRequestParse(inputStream, logger);

        // then
        Assertions.assertThat(parsedRequest.getMethod()).isEqualTo("GET");
        Assertions.assertThat(parsedRequest.getUri()).isEqualTo("/index.html");
        Assertions.assertThat(parsedRequest.getPath()).isEqualTo("./src/main/resources/templates/index.html");
        Assertions.assertThat(parsedRequest.getContentType()).isEqualTo("html");

    }
}

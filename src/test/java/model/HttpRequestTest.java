package model;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

class HttpRequestTest {

    @DisplayName("request를 적절할게 파싱해준다.")
    @Test
    void httpRequestFrom() throws IOException {
        //given
        String mockRequest = "GET /index.html HTTP/1.1\n" +
                "Host: localhost:8080\n" +
                "Connection: keep-alive\n" +
                "Accept: */*\n" +
                "Content-Length: 23\n\n" +
                "request body";

        InputStream inputStream = new ByteArrayInputStream(mockRequest.getBytes());

        //when
        HttpRequest httpRequest = HttpRequest.from(inputStream);

        //then
        Assertions.assertThat(httpRequest)
                .extracting("method", "uri", "host", "connection", "accept")
                .contains("GET", URI.create("/index.html"), "localhost:8080", "keep-alive", "*/*");
    }

}
package http;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import webserver.http.HttpRequest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

import static org.assertj.core.api.Assertions.assertThat;

class HttpRequestTest {

    @Test
    @DisplayName("BufferedReader를 받아 HttpRequest 반환")
    void testCreateFromReader() throws IOException {
        String requestString = "GET /index.html?name=hong&age=26 HTTP/1.1\r\n" +
                "Accept: text/html,*/*\r\n" +
                "Accept-Encoding: gzip, deflate, br\r\n" +
                "Accept-Language: ko-KR,ko;q=0.9,en-US;q=0.8,en;q=0.7\r\n" +
                "Connection: keep-alive\r\n" +
                "Host: localhost:8080\r\n" +
                "User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36\r\n\r\n";

        BufferedReader bufferedReader = new BufferedReader(new StringReader(requestString));

        HttpRequest httpRequest = HttpRequest.createFromReader(bufferedReader);

        assertThat(httpRequest.getMethod()).isEqualTo("GET");
        assertThat(httpRequest.getUrl()).isEqualTo("/index.html");
        assertThat(httpRequest.getQueryString())
                .containsEntry("name", "hong")
                .containsEntry("age", "26");

        assertThat(httpRequest.getHeaderProperty("Host")).isEqualTo("localhost:8080");
        assertThat(httpRequest.getHeaderProperty("Accept")).isEqualTo("text/html,*/*");
        assertThat(httpRequest.getHeaderProperty("Connection")).isEqualTo("keep-alive");

        assertThat(httpRequest.getBody())
                .hasFieldOrPropertyWithValue("rawString","");
    }
}

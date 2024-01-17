package utils;

import model.HttpRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.*;

import static org.junit.jupiter.api.Assertions.assertThrows;

class HttpUtilTest {
    @Test
    @DisplayName("BufferedReader를 받아 HttpRequest 반환")
    void testGetHttpRequest() throws IOException {
        String requestString = "GET /index.html HTTP/1.1\r\n" +
                "Accept: text/html,*/*\r\n" +
                "Accept-Encoding: gzip, deflate, br\r\n" +
                "Accept-Language: ko-KR,ko;q=0.9,en-US;q=0.8,en;q=0.7\r\n" +
                "Connection: keep-alive\r\n" +
                "Host: localhost:8080\r\n" +
                "User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36\r\n";
        BufferedReader bufferedReader = new BufferedReader(new StringReader(requestString));

        HttpRequest httpRequest = HttpUtil.getHttpRequest(bufferedReader);

        assertThat(httpRequest.getMethod()).isEqualTo("GET");
        assertThat(httpRequest.getUrl()).isEqualTo("/index.html");
        assertThat(httpRequest.getQueryString()).isEmpty();
    }

    @Test
    @DisplayName("Url을 받아 알맞은 Content-Type으로 반환")
    void testGetContentTypeFromUrl() throws IOException {
        String htmlUrl = "/index.html";
        String htmlContentType = HttpUtil.getContentTypeFromUrl(htmlUrl);
        String javascriptUrl = "/index.js";
        String javascriptContentType = HttpUtil.getContentTypeFromUrl(javascriptUrl);
        String cssUrl = "/index.css";
        String cssContentType = HttpUtil.getContentTypeFromUrl(cssUrl);
        String ttfUrl = "/index.ttf";
        String ttfContentType = HttpUtil.getContentTypeFromUrl(ttfUrl);
        String woffUrl = "/index.woff";
        String woffContentType = HttpUtil.getContentTypeFromUrl(woffUrl);

        assertThat(htmlContentType).isEqualTo("text/html");
        assertThat(javascriptContentType).isEqualTo("text/javascript");
        assertThat(cssContentType).isEqualTo("text/css");
        assertThat(ttfContentType).isEqualTo("font/ttf");
        assertThat(woffContentType).isEqualTo("font/woff");

        assertThrows(IOException.class, ()->{
            String errorUrl = "/index.error";
            String errorContentType = HttpUtil.getContentTypeFromUrl(errorUrl);
        });
    }
}
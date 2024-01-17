package parser;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import webserver.header.RequestHeader;

import java.io.*;

import static org.junit.jupiter.api.Assertions.*;

class RequestHeaderParserTest {
    @Test
    @DisplayName("Request Header가 정상적으로 파싱되는지 테스트")
    void parseRequestHeaderTest() throws IOException {
        InputStream inputStream = getInputStream();
        RequestHeader requestHeader = RequestHeaderParser.parse(inputStream);

        assertEquals("GET", requestHeader.getMethod());
        assertEquals("/js/scripts.js", requestHeader.getPath());
    }

    private static InputStream getInputStream() {
        String originRequestHeader = "GET /js/scripts.js HTTP/1.1\n" +
                "Host: localhost:8080\n" +
                "Sec-Fetch-Site: same-origin\n" +
                "Accept-Encoding: gzip, deflate\n" +
                "Connection: keep-alive\n" +
                "Sec-Fetch-Mode: no-cors\n" +
                "Accept: */*\n" +
                "User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/17.2.1 Safari/605.1.15\n" +
                "Referer: http://localhost:8080/index.html\n" +
                "Sec-Fetch-Dest: script\n" +
                "Accept-Language: ko-KR,ko;q=0.9";

        InputStream inputStream = new ByteArrayInputStream(originRequestHeader.getBytes());
        return inputStream;
    }
}
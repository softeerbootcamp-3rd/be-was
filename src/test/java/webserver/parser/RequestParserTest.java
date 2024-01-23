package webserver.parser;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import webserver.exception.GeneralException;
import webserver.request.Request;

import java.io.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class RequestParserTest {
    @Test
    @DisplayName("Request가 정상적으로 파싱되는지 테스트")
    void parseRequestTest() throws IOException {
        InputStream inputStream = getRequest();
        Request request = RequestParser.parse(inputStream);

        assertEquals("GET", request.getMethod());
        assertEquals("/user/create", request.getPath());
        assertEquals("user", request.getParam("userId"));
        assertEquals("password", request.getParam("password"));
        assertEquals("name", request.getParam("name"));
        assertEquals("ab@bc.cd", request.getParam("email"));
        assertEquals("user", request.getBody("userId"));
        assertEquals("password", request.getBody("password"));
        assertEquals("name", request.getBody("name"));
        assertEquals("ab@bc.cd", request.getBody("email"));
    }

    @Test
    @DisplayName("Request의 파라미터가 잘못될 경우 예외 발생 테스트")
    void parseInvalidRequestTest() throws IOException {
        InputStream inputStream = getInvalidRequest();

        assertThrows(GeneralException.class, () -> {
            RequestParser.parse(inputStream);
        });
    }

    private static InputStream getInvalidRequest() {
        String originRequestHeader = "GET /user/create?userId=user&userId=password&name=name&email=ab@bc.cd HTTP/1.1\n" +
                "Host: localhost:8080\n" +
                "Sec-Fetch-Site: same-origin\n" +
                "Accept-Encoding: gzip, deflate\n" +
                "Connection: keep-alive\n" +
                "Sec-Fetch-Mode: no-cors\n" +
                "Accept: */*\n" +
                "User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/17.2.1 Safari/605.1.15\n" +
                "Referer: http://localhost:8080/index.html\n" +
                "Sec-Fetch-Dest: script\n" +
                "Accept-Language: ko-KR,ko;q=0.9\n" +
                "\r\n" +
                "userId=user&password=password&name=name&email=ab@bc.cd";

        return new ByteArrayInputStream(originRequestHeader.getBytes());
    }

    private static InputStream getRequest() {
        String originRequestHeader = "GET /user/create?userId=user&password=password&name=name&email=ab@bc.cd HTTP/1.1\n" +
                "Host: localhost:8080\n" +
                "Sec-Fetch-Site: same-origin\n" +
                "Accept-Encoding: gzip, deflate\n" +
                "Connection: keep-alive\n" +
                "Sec-Fetch-Mode: no-cors\n" +
                "Accept: */*\n" +
                "User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/17.2.1 Safari/605.1.15\n" +
                "Referer: http://localhost:8080/index.html\n" +
                "Sec-Fetch-Dest: script\n" +
                "Accept-Language: ko-KR,ko;q=0.9\n" +
                "\r\n" +
                "userId=user&password=password&name=name&email=ab@bc.cd";

        return new ByteArrayInputStream(originRequestHeader.getBytes());
    }
}
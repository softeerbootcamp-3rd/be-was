package webserver.http;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import webserver.http.constants.Mime;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

import static org.junit.jupiter.api.Assertions.*;

class GetRequestTest {
    private String httpRequest = "GET /user/create?userId=123&password=123&name=123&email=1%403 HTTP/1.1\n" +
            "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7\n" +
            "Accept-Encoding: gzip, deflate, br\n" +
            "Accept-Language: ko,en-US;q=0.9,en;q=0.8,ko-KR;q=0.7,ja;q=0.6\n" +
            "Connection: keep-alive\n" +
            "Host: localhost:8080\n" +
            "Referer: http://localhost:8080/user/form.html\n" +
            "Sec-Fetch-Dest: document\n" +
            "Sec-Fetch-Mode: navigate\n" +
            "Sec-Fetch-Site: same-origin\n" +
            "Sec-Fetch-User: ?1\n" +
            "Upgrade-Insecure-Requests: 1\n" +
            "User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36\n" +
            "sec-ch-ua: \"Not_A Brand\";v=\"8\", \"Chromium\";v=\"120\", \"Google Chrome\";v=\"120\"\n" +
            "sec-ch-ua-mobile: ?0\n" +
            "sec-ch-ua-platform: \"macOS\"";
    private Request request;


    @BeforeEach
    void setUp() throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new StringReader(httpRequest));
        request = new Request(bufferedReader);
    }

    @Test
    @DisplayName("사용자가 User를 생성했을때 데이터가 정상적으로 파싱이 된다.")
    void getRequestTarget() {
        //GIVEN

        //WHEN
        String expected = "/user/create?userId=123&password=123&name=123&email=1%403";
        String actual = request.getRequestTarget();
        //THEN
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("사용자가 유저를 생성했을때 요청되는 MIME TYPE이 NONE값이다.")
    void getResponseMimeType() {
        //GIVEN

        //WHEN
        Mime expected = Mime.NONE;
        Mime actual = request.getResponseMimeType();
        //THEN
        assertEquals(expected, actual);
    }
}
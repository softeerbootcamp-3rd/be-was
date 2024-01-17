import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import utils.RequestParser;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


public class RequestParserTest {


    @BeforeAll
    static void init() throws IOException {
        String validRequest = "GET /index.html HTTP/1.1\n" +
                "Host: localhost:8080\n" +
                "Connection: keep-alive\n" +
                "Cache-Control: max-age=0\n" +
                "sec-ch-ua: \"Not_A Brand\";v=\"8\", \"Chromium\";v=\"120\", \"Google Chrome\";v=\"120\"\n" +
                "sec-ch-ua-mobile: ?0\n" +
                "sec-ch-ua-platform: \"macOS\"\n" +
                "Upgrade-Insecure-Requests: 1\n" +
                "User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36\n" +
                "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7\n" +
                "Sec-Fetch-Site: same-origin\n" +
                "Sec-Fetch-Mode: navigate\n" +
                "Sec-Fetch-User: ?1\n" +
                "Sec-Fetch-Dest: document\n" +
                "Referer: http://localhost:8080/user/login.html\n" +
                "Accept-Encoding: gzip, deflate, br\n" +
                "Accept-Language: ko-KR,ko;q=0.9,en-US;q=0.8,en;q=0.7\n" +
                "Cookie: Idea-69015365=c84b7ca8-76bc-4946-8364-2ed31c35b3d4";

        InputStream validRequestStream = new ByteArrayInputStream(validRequest.getBytes());
        RequestParser.parseHeaders(validRequestStream);
    }

    void makeInvalidRequest() throws IOException {
        String invalidRequest = "";
        InputStream invalidRequestStream = new ByteArrayInputStream(invalidRequest.getBytes());
        RequestParser.parseHeaders(invalidRequestStream);
    }

    @Test
    void MethodParsing(){
        assertThat("GET").isEqualTo(RequestParser.getMethod());
    }

    @Test
    void HostParsing(){
        assertThat("localhost:8080").isEqualTo(RequestParser.getHost());
    }

    @Test
    void PathParsing(){
        assertThat("/index.html").isEqualTo(RequestParser.getPath());
    }

    @Test
    void AcceptParsing(){
        assertThat("text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7").isEqualTo(RequestParser.getAccept());
    }

    @Test
    void UserAgentParsing(){
        assertThat("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36").isEqualTo(RequestParser.getUserAgent());
    }

    @Test
    void CookieParsing(){
        assertThat("Idea-69015365=c84b7ca8-76bc-4946-8364-2ed31c35b3d4").isEqualTo(RequestParser.getCookie());
    }

    @Test
    void Should_ThrowException_When_No_Request() throws IOException {
        assertThatThrownBy(this::makeInvalidRequest);
    }

}

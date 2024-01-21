import org.junit.jupiter.api.BeforeAll;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


public class RequestParserTest {
    private static InputStream validRequestStream;
    @BeforeAll
    static void init() throws IOException {

        StringBuilder validRequest = new StringBuilder();
        validRequest.append("GET /index.html HTTP/1.1\n")
                .append("Host: localhost:8080\n")
                .append("Connection: keep-alive\n")
                .append("Cache-Control: max-age=0\n")
                .append("sec-ch-ua: \"Not_A Brand\";v=\"8\", \"Chromium\";v=\"120\", \"Google Chrome\";v=\"120\"\n")
                .append("sec-ch-ua-mobile: ?0\n")
                .append("sec-ch-ua-platform: \"macOS\"\n")
                .append("Upgrade-Insecure-Requests: 1\n")
                .append("User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36\n")
                .append("Accept: text/html,application/xhtml+xml,application/xml\n")
                .append("Sec-Fetch-Site: same-origin\n")
                .append("Sec-Fetch-Mode: navigate\n")
                .append("Sec-Fetch-User: ?1\n")
                .append("Sec-Fetch-Dest: document\n")
                .append("Referer: http://localhost:8080/user/login.html\n")
                .append("Accept-Encoding: gzip, deflate, br\n")
                .append("Accept-Language: ko-KR,ko;q=0.9,en-US;q=0.8,en;q=0.7\n")
                .append("Cookie: Idea-69015365=c84b7ca8-76bc-4946-8364-2ed31c35b3d4")
                .append("\r\n\r\n")
                .append("12345678");
//        String validRequest = "GET /user/create?userId=javajigi&password=password&name=%EB%B0%95%EC%9E%AC%EC%84%B1&email=javajigi%40slipp.net HTTP/1.1\n" +
//                "Host: localhost:8080" +
//                "Connection: keep-allive" +
//                "Accept: */*";
        validRequestStream = new ByteArrayInputStream(validRequest.toString().getBytes());

    }
    
}

package webserver;

import db.Database;
import db.SessionManager;
import model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import webserver.http.Request;
import webserver.http.RequestHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.stream.StreamSupport;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LoginTest {
    private String requestBody = "userId=123&password=123";
    private String httpRequest =
            "POST /user/login HTTP/1.1\n" +
                    "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7\n" +
                    "Accept-Encoding: gzip, deflate, br\n" +
                    "Accept-Language: ko,en-US;q=0.9,en;q=0.8,ko-KR;q=0.7,ja;q=0.6\n" +
                    "Connection: keep-alive\n" +
                    "Content-Type: application/x-www-form-urlencoded\n" +
                    "Content-Length: " + requestBody.length() + "\n" +
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
                    "sec-ch-ua-platform: \"macOS\"\n\n" +
                    requestBody;

    private RequestHandler requestHandler = new RequestHandler();
    private Request request;

    @BeforeEach
    void setUp() throws IOException {
        String setUpRequestBody = "userId=123&password=123&name=123&email=1%403";
        String setUpHttpRequest =
                "POST /user/create HTTP/1.1\n" +
                        "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7\n" +
                        "Accept-Encoding: gzip, deflate, br\n" +
                        "Accept-Language: ko,en-US;q=0.9,en;q=0.8,ko-KR;q=0.7,ja;q=0.6\n" +
                        "Connection: keep-alive\n" +
                        "Content-Type: application/x-www-form-urlencoded\n" +
                        "Content-Length: " + setUpRequestBody.length() + "\n" +
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
                        "sec-ch-ua-platform: \"macOS\"\n\n" +
                        setUpRequestBody;
        BufferedReader bufferedReader = new BufferedReader(new StringReader(setUpHttpRequest));
        request = new Request(bufferedReader);
        requestHandler.handleRequest(request);

        bufferedReader = new BufferedReader(new StringReader(httpRequest));
        request = new Request(bufferedReader);
    }

    @Test
    @DisplayName("사용자가 로그인했을시 Session을 받아올 수 있다.")
    void getRequestTarget() {
        //GIVEN
        //WHEN
        requestHandler.handleRequest(request);
        //THEN
        User expected = new User("123", "123", "123", "1@3");
        String val = request.getRequestHeader().get("Set-Cookie").split(";")[0].split("=")[1];
        User actual = SessionManager.findUserById(val);
        assertEquals(expected, actual);
    }
}

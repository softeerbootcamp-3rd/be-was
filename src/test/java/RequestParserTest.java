import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import utils.HttpRequestParser;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


public class RequestParserTest {
    @Test
    void messageParsing() throws IOException {
        //given
        String requestMessage = "POST /user/create HTTP/1.1\r\n" +
                "Host: localhost:8080\r\n" +
                "Connection: keep-alive\r\n" +
                "Content-Length: 59\r\n" +
                "Content-Type: application/x-www-form-urlencoded\r\n" +
                "Accept: */*\r\n" +
                "\r\n" +
                "userId=javajigi&password=password&name=%EB%B0%95%EC%9E%AC%EC%84%B1&email=javajigi%40slipp.net";
        InputStream in = new ByteArrayInputStream(requestMessage.getBytes());
        String body = "userId=javajigi&password=password&name=%EB%B0%95%EC%9E%AC%EC%84%B1&email=javajigi%40slipp.net";
        String length = "59";

        //when
        HttpRequestParser parser = new HttpRequestParser();

        //then
        assertThat(parser.parseHttpRequest(in).get("Body")).isEqualTo(body);

    }

    @Test
    void emptyBodyParsing() throws IOException {
        //given
        String requestMessage = "GET /index.html HTTP/1.1\r\n"
                + "Host: localhost:8080\r\n"
                + "Connection: keep-alive\r\n"
                + "Accept: */*\r\n\r\n";
        InputStream in = new ByteArrayInputStream(requestMessage.getBytes());

        //when
        HttpRequestParser parser = new HttpRequestParser();

        //then
        assertThat(parser.parseHttpRequest(in).get("Body")).isEmpty();
    }

}

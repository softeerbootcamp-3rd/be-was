package model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.assertj.core.api.Assertions.assertThat;

class HttpRequestTest {

    @DisplayName("request를 적절할게 파싱해준다. - GET")
    @Test
    void httpRequestFromWithGET() throws IOException {
        //given
        String mockRequest = createMockRequestWithGET();
        InputStream inputStream = new ByteArrayInputStream(mockRequest.getBytes());

        //when
        HttpRequest httpRequest = HttpRequest.from(inputStream);

        //then
        String httpRequestToString = httpRequest.toString();
        assertThat(httpRequestToString).isEqualTo("HttpRequest{\n"+
                "method=GET,\n" +
                "uri=/index.html,\n" +
                "headers={Accept:=*/*, Host:=localhost:8080, Connection:=keep-alive},\n" +
                "body=null\n}");
    }

    @DisplayName("request를 적절하게 파싱해준다. - POST")
    @Test
    void httpRequestFromWithPOST() throws IOException {
        //given
        String mockRequest = createMockRequestWithPOST();
        InputStream inputStream = new ByteArrayInputStream(mockRequest.getBytes());

        //when
        HttpRequest httpRequest = HttpRequest.from(inputStream);

        //then
        String httpRequestToString = httpRequest.toString();
        assertThat(httpRequestToString).isEqualTo("HttpRequest{" + "\n" +
                "method=POST,\n" +
                "uri=/user/create,\n" +
                "headers={Accept:=*/*, Content-Length:=59, Host:=localhost:8080, Connection:=keep-alive},\n" +
                "body=userId=test1&password=1234&name=test&email=test@naver.com\n}");
    }

    private static String createMockRequestWithPOST() throws IOException {
        return "POST /user/create HTTP/1.1\n" +
                "Host: localhost:8080\n" +
                "Connection: keep-alive\n" +
                "Accept: */*\n" +
                "Content-Length: 59\n\n" +
                "userId=test1&password=1234&name=test&email=test@naver.com";
    }

    private static String createMockRequestWithGET() throws IOException {
        return "GET /index.html HTTP/1.1\n" +
                "Host: localhost:8080\n" +
                "Connection: keep-alive\n" +
                "Accept: */*\n\n";
    }
}

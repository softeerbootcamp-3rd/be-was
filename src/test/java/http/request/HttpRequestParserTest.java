package http.request;

import http.request.parser.HttpRequestParser;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.assertj.core.api.Assertions.assertThat;
import java.util.HashMap;

public class HttpRequestParserTest {

    @Test
    @DisplayName("POST 요청 문자열 파싱 테스트")
    public void parseHttpRequestTest() {
        // 테스트를 위한 HTTP 요청 문자열 예시
        String requestText = "POST /user/create HTTP/1.1\n" +
                "Host: localhost:8080\n" +
                "Content-Type: application/x-www-form-urlencoded\n" +
                "\r\n" +
                "userId=testUser&password=1234";

        // HttpRequest 객체 생성
        HttpRequest request = new HttpRequest(requestText);
        request.setHeaders(new HashMap<>()); // 헤더 초기화
        request.setBodyParams(new HashMap<>()); // 바디 파라미터 초기화

        // HttpRequestParser를 사용하여 파싱
        HttpRequestParser.parse(request, requestText);

        // 요청 라인 검증
        assertThat(request.getMethod()).isEqualTo("POST");
        assertThat(request.getPath()).isEqualTo("/user/create");

        // 헤더 검증
        assertThat(request.getHeaders()).containsEntry("Host", "localhost:8080");
        assertThat(request.getHeaders()).containsEntry("Content-Type", "application/x-www-form-urlencoded");

        // 본문 파라미터 검증
        assertThat(request.getBodyParams()).containsEntry("userId", "testUser");
        assertThat(request.getBodyParams()).containsEntry("password", "1234");
    }
}

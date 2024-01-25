package util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.*;

public class HttpRequestHeaderUtilsTest {

    String requestMessage;
    @BeforeEach
    void init() {
        requestMessage = getQueryStringRequestMessage();
    }

    private String getQueryStringRequestMessage() {
        StringBuilder sb = new StringBuilder();
        sb.append("GET /user/create?userId=hello&password=asd&name=swk&email=test@naver.com HTTP/1.1").append("\r\n");
        sb.append("Host: localhost:8080").append("\r\n");
        sb.append("Accept: text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8").append("\r\n");
        sb.append("User-Agent: Mozilla/4.0(compatible; MSIE 6.0; Windows NT 5.1; SV1; .NET CLR 1.1.4322; InfoPath.1)").append("\r\n");
        sb.append("Cookie: Idea-14db9320=4d059d9c-532a-48d2-8ebe-3a46dcd4402f").append("\r\n");
        sb.append("\r\n");

        return sb.toString();
    }

    private String getNoQueryStringRequestMessage() {
        StringBuilder sb = new StringBuilder();
        sb.append("GET /user/create HTTP/1.1").append("\r\n");
        sb.append("Host: localhost:8080").append("\r\n");
        sb.append("Accept: text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8").append("\r\n");
        sb.append("User-Agent: Mozilla/4.0(compatible; MSIE 6.0; Windows NT 5.1; SV1; .NET CLR 1.1.4322; InfoPath.1)").append("\r\n");
        sb.append("Cookie: Idea-14db9320=4d059d9c-532a-48d2-8ebe-3a46dcd4402f").append("\r\n");
        sb.append("\r\n");
		sb.append("userId=hello&password=asd&name=swk&email=test@naver.com");

        return sb.toString();
    }

    @Test
    @DisplayName("요청 메서드 테스트")
    void 요청_메서드_테스트(){
        //given
        HttpRequestHeaderUtils httpRequestHeaderUtils = HttpRequestHeaderUtils.createHeaderUtils(requestMessage);

        //when
        String requestMethod = httpRequestHeaderUtils.getRequestMethod();

        //then
        assertThat(requestMethod).isEqualTo("GET");
    }

    @Test
    @DisplayName("요청 uri 테스트")
    void 요청_URI_테스트(){
        //given
        HttpRequestHeaderUtils httpRequestHeaderUtils = HttpRequestHeaderUtils.createHeaderUtils(requestMessage);

        //when
        String requestPath = httpRequestHeaderUtils.getRequestUri();

        //then
        assertThat(requestPath).isEqualTo("/user/create");
    }

    @Test
    @DisplayName("쿼리 스트링 있는 경우 테스트")
    void 쿼리_스트링_O_테스트(){
        //given
        HttpRequestHeaderUtils httpRequestHeaderUtils = HttpRequestHeaderUtils.createHeaderUtils(requestMessage);

        //when
        String queryString = httpRequestHeaderUtils.getQueryString();

        //then
        assertThat(queryString).isEqualTo("userId=hello&password=asd&name=swk&email=test@naver.com");
    }
    @Test
    @DisplayName("쿼리 스트링 없는 경우 테스트")
    void 쿼리_스트링_X_테스트(){
        //given
        requestMessage = getNoQueryStringRequestMessage();
        HttpRequestHeaderUtils httpRequestHeaderUtils = HttpRequestHeaderUtils.createHeaderUtils(requestMessage);

        //when
        String queryString = httpRequestHeaderUtils.getQueryString();

        //then
        assertThat(queryString).isEqualTo("None");
    }

    @Test
    @DisplayName("Host 필드 테스트")
    void Host_필드_테스트(){
        //given
        HttpRequestHeaderUtils httpRequestHeaderUtils = HttpRequestHeaderUtils.createHeaderUtils(requestMessage);

        //when
        Map<String, String> requestHeaders = httpRequestHeaderUtils.getRequestHeaders();
        String host = requestHeaders.get("Host");

        //then
        assertThat(host).isEqualTo("localhost:8080");
    }

    @Test
    @DisplayName("User-Agent 필드 테스트")
    void User_Agent_필드_테스트(){
        //given
        HttpRequestHeaderUtils httpRequestHeaderUtils = HttpRequestHeaderUtils.createHeaderUtils(requestMessage);

        //when
        Map<String, String> requestHeaders = httpRequestHeaderUtils.getRequestHeaders();
        String userAgent = requestHeaders.get("User-Agent");

        //then
        assertThat(userAgent).isEqualTo("Mozilla/4.0(compatible; MSIE 6.0; Windows NT 5.1; SV1; .NET CLR 1.1.4322; InfoPath.1)");
    }

    @Test
    @DisplayName("Accept 필드 테스트")
    void Accept_필드_테스트(){
        //given
        HttpRequestHeaderUtils httpRequestHeaderUtils = HttpRequestHeaderUtils.createHeaderUtils(requestMessage);

        //when
        Map<String, String> requestHeaders = httpRequestHeaderUtils.getRequestHeaders();
        String accept = requestHeaders.get("Accept");

        //then
        assertThat(accept).isEqualTo("text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
    }

    @Test
    @DisplayName("Cookie 필드 테스트")
    void Cookie_필드_테스트(){
        //given
        HttpRequestHeaderUtils httpRequestHeaderUtils = HttpRequestHeaderUtils.createHeaderUtils(requestMessage);

        //when
        Map<String, String> requestHeaders = httpRequestHeaderUtils.getRequestHeaders();
        String cookie = requestHeaders.get("Cookie");
        //then
        assertThat(cookie).isEqualTo("Idea-14db9320=4d059d9c-532a-48d2-8ebe-3a46dcd4402f");
    }
}

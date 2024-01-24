package db;

import controller.HttpStatusCode;
import data.CookieData;
import data.Response;
import org.junit.jupiter.api.*;

import static org.assertj.core.api.Assertions.assertThat;

public class ResponseTest {

    @Test
    @DisplayName("쿠키 없는 응답 데이터 생성")
    public void testResponseWithoutCookie() {
        // Given
        HttpStatusCode status = HttpStatusCode.OK;
        String path = "/example";

        // When
        Response response = new Response(status, path);

        // Then
        // 상태와 경로가 의도대로 설정되었는지 검증
        assertThat(response.getStatus()).isEqualTo(status);
        assertThat(response.getPath()).isEqualTo(path);

        // 쿠키가 비어있는지 검증
        assertThat(response.getCookie()).isNull();

        // toString() 메서드 검증
        assertThat(response.toString()).isEqualTo("Response : " + status + "\n" +
                "Cookie : null\n");
    }

    @Test
    @DisplayName("쿠키 있는 응답 데이터 생성")
    public void testResponseWithCookie() {
        // Given
        HttpStatusCode status = HttpStatusCode.NOT_FOUND;
        String path = "/test";
        CookieData cookie = new CookieData("user", 123);

        // When
        Response response = new Response(status, path, cookie);

        // Then
        // 상태, 경로, 쿠키가 의도대로 설정되었는지 검증
        assertThat(response.getStatus()).isEqualTo(status);
        assertThat(response.getPath()).isEqualTo(path);
        assertThat(response.getCookie()).isEqualTo(cookie);

        // toString() 메서드 검증
        assertThat(response.toString()).isEqualTo("Response : " + status + "\n" +
                "Cookie : " + cookie + "\n");
    }
}


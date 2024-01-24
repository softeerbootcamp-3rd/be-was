package domain.user.command.application;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import common.http.response.HttpResponse;
import common.http.response.HttpStatusCode;
import java.util.Map;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import webserver.container.ResponseThreadLocal;

@DisplayName("UserCreateServiceTest 클래스")
class UserCreateServiceTest {
    private UserCreateService userCreateService = new UserCreateService();
    private HttpResponse httpResponse;

    @BeforeEach
    void setUp() {
        httpResponse = new HttpResponse(null, null, null);
        ResponseThreadLocal.setHttpResponse(httpResponse);
    }

    @AfterEach
    void tearDown() {
        ResponseThreadLocal.clearHttpResponse();
    }

    @Test
    @DisplayName("정상 요청 시 유저 생성 테스트")
    void valid_makeNewUser() {
        // given
        UserCreateRequest userCreateRequest = new UserCreateRequest("userId", "password", "name", "email");

        // when
        userCreateService.makeNewUser(userCreateRequest);

        // then
        HttpResponse httpResponse = ResponseThreadLocal.getHttpResponse();

        assertEquals(httpResponse.getStartLine().getStatusCode(), HttpStatusCode.FOUND);

        Map<String, String> headers = httpResponse.getHeader().getHeaders();
        assertNotNull(headers.get("Location"));
        assertEquals(headers.get("Location"), "/index.html");
    }

}

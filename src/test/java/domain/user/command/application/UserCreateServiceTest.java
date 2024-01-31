package domain.user.command.application;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import common.db.Database;
import common.http.response.HttpResponse;
import common.http.response.HttpStatusCode;
import domain.user.command.domain.User;
import java.util.Map;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import webserver.container.CustomThreadLocal;

@DisplayName("UserCreateServiceTest 클래스")
class UserCreateServiceTest {
    private UserCreateService userCreateService = new UserCreateService();
    private HttpResponse httpResponse;

    @BeforeEach
    void setUp() {
        Database.Users().clear();
        httpResponse = new HttpResponse(null, null, null);
        CustomThreadLocal.setHttpResponse(httpResponse);
    }

    @AfterEach
    void tearDown() {
        Database.Users().clear();
        CustomThreadLocal.clearHttpResponse();
    }

    @Test
    @DisplayName("정상 요청 시 유저 생성 테스트")
    void valid_makeNewUser() {
        // given
        UserCreateRequest userCreateRequest = new UserCreateRequest("userId", "password", "name", "email");

        // when
        userCreateService.makeNewUser(userCreateRequest);

        // then
        User dbUser = Database.Users().get("userId");
        assertEquals(dbUser.getUserId(), userCreateRequest.getUserId());

        HttpResponse httpResponse = CustomThreadLocal.getHttpResponse();

        assertEquals(httpResponse.getStartLine().getStatusCode(), HttpStatusCode.FOUND);

        Map<String, String> headers = httpResponse.getHeader().getHeaders();
        assertNotNull(headers.get("Location"));
        assertEquals(headers.get("Location"), "/index.html");
    }

    @Test
    @DisplayName("유저 중복시 BAD_REQUEST 테스트")
    void duplicatedUserIdRequest() {
        // given
        Database.Users().put("1", new User("1", "password", "name", "email"));
        UserCreateRequest userCreateRequest = new UserCreateRequest("1", "password", "name", "email");

        // when
        userCreateService.makeNewUser(userCreateRequest);

        // then
        HttpResponse httpResponse = CustomThreadLocal.getHttpResponse();

        assertEquals(Database.Users().size(), 1);
        assertEquals(httpResponse.getStartLine().getStatusCode(), HttpStatusCode.BAD_REQUEST);
    }
}

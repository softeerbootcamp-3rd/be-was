package domain.user.presentation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import common.db.Database;
import common.http.response.HttpResponse;
import common.http.response.HttpStatusCode;
import domain.user.command.application.UserCreateRequest;
import domain.user.command.domain.User;
import java.util.stream.Stream;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import webserver.container.CustomThreadLocal;

@DisplayName("UserControllerTest 클래스")
class UserControllerTest {
    private UserController userController = new UserController();
    private HttpResponse httpResponse;


    @BeforeEach
    void setUp() {
        Database.Users().clear();
        Database.SessionStorage().clear();
        httpResponse = new HttpResponse(null, null, null);
        CustomThreadLocal.setHttpResponse(httpResponse);
    }

    @AfterEach
    void tearDown() {
        Database.Users().clear();
        Database.SessionStorage().clear();
        CustomThreadLocal.clearHttpResponse();
    }

    @Test
    @DisplayName("회원가입 정상 요청 시 테스트")
    void valid_signup() {
        // given
        UserCreateRequest userCreateRequest = new UserCreateRequest("1", "password", "name", "email");

        // when
        userController.signup(userCreateRequest);

        // then
        HttpResponse httpResponse = CustomThreadLocal.getHttpResponse();
        assertEquals(Database.Users().size(), 1);
        assertEquals(httpResponse.getStartLine().getStatusCode(), HttpStatusCode.FOUND);
        assertEquals(httpResponse.getHeader().getHeaders().get("Location"), "/index.html");
    }

    @ParameterizedTest
    @MethodSource("invalidSignupRequestProvider")
    @DisplayName("회원가입 비정상 요청 시 테스트")
    void invalid_signup(UserCreateRequest userCreateRequest) {
        // given

        // when
        userController.signup(userCreateRequest);

        // then
        HttpResponse httpResponse = CustomThreadLocal.getHttpResponse();
        assertEquals(Database.Users().size(), 0);
        assertEquals(httpResponse.getStartLine().getStatusCode(), HttpStatusCode.BAD_REQUEST);
    }

    private static Stream<Arguments> invalidSignupRequestProvider() {
        return Stream.of(
            Arguments.of(new UserCreateRequest(null, "password", "name", "email")),
            Arguments.of(new UserCreateRequest("1", null, "name", "email")),
            Arguments.of(new UserCreateRequest("1", "password1", null, "email")),
            Arguments.of(new UserCreateRequest("1", "password1", "name", null))
        );
    }

    @Test
    @DisplayName("로그인 정상 요청 시 테스트")
    void valid_login() {
        // given
        Database.Users().put("1", new User("1", "password", "name", "email"));
        UserLoginRequest userLoginRequest = new UserLoginRequest("1", "password");

        // when
        userController.login(userLoginRequest);

        // then
        assertEquals(Database.SessionStorage().size(), 1);
        assertNotNull(httpResponse.getHeader().getHeaders().get("Set-Cookie"));

        assertEquals(httpResponse.getStartLine().getStatusCode(), HttpStatusCode.FOUND);
        assertEquals(httpResponse.getHeader().getHeaders().get("Location"), "/index.html");
        assertEquals(httpResponse.getHeader().getHeaders().get("Content-Type"), "text/html");
    }

    @ParameterizedTest
    @MethodSource("invalidLoginRequestProvider")
    @DisplayName("로그인 비정상 요청 시 테스트")
    void invalid_login(UserLoginRequest userLoginRequest) {
        // given
        Database.Users().put("1", new User("1", "password", "name", "email"));

        // when
        userController.login(userLoginRequest);

        // then
        HttpResponse httpResponse = CustomThreadLocal.getHttpResponse();
        assertEquals(httpResponse.getStartLine().getStatusCode(), HttpStatusCode.FOUND);
        assertEquals(httpResponse.getHeader().getHeaders().get("Location"), "/user/login_failed.html");
    }

    private static Stream<Arguments> invalidLoginRequestProvider() {
        return Stream.of(
            Arguments.of(new UserLoginRequest(null, "password")),
            Arguments.of(new UserLoginRequest("1", null))
        );
    }
}

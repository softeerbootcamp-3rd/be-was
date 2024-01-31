package domain.user.command.application;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import common.db.Database;
import common.http.response.HttpResponse;
import common.http.response.HttpStatusCode;
import domain.user.command.domain.User;
import domain.user.presentation.UserLoginRequest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import webserver.container.CustomThreadLocal;

@DisplayName("UserLoginServiceTest 클래스")
class UserLoginServiceTest {
    private UserLoginService userLoginService = new UserLoginService();
    private HttpResponse httpResponse;

    @BeforeEach
    void setUp() {
        Database.SessionStorage().clear();
        httpResponse = new HttpResponse(null, null, null);
        CustomThreadLocal.setHttpResponse(httpResponse);
    }

    @AfterEach
    void tearDown() {
        CustomThreadLocal.clearHttpResponse();
    }

    @Test
    @DisplayName("정상 요청 시 로그인 테스트")
    void valid_login() {
        // given
        Database.Users().put("1", new User("1", "password", "name", "email"));
        UserLoginRequest userLoginRequest = new UserLoginRequest("1", "password");

        // when
        userLoginService.login(userLoginRequest);

        // then
        assertEquals(Database.SessionStorage().size(), 1);

        HttpResponse httpResponse = CustomThreadLocal.getHttpResponse();
        String cookieValue = httpResponse.getHeader().getHeaders().get("Set-Cookie");
        Pattern pattern = Pattern.compile("sid=([^;]*)");
        Matcher matcher = pattern.matcher(cookieValue);
        String sessionId = "";

        if (matcher.find()) {
            sessionId = matcher.group(1);
        }

        assertNotNull(httpResponse.getHeader().getHeaders().get("Set-Cookie"));
        assertEquals(Database.SessionStorage().get(sessionId), "1");

        assertEquals(httpResponse.getStartLine().getStatusCode(), HttpStatusCode.FOUND);
        assertEquals(httpResponse.getHeader().getHeaders().get("Location"), "/index.html");
        assertEquals(httpResponse.getHeader().getHeaders().get("Content-Type"), "text/html");
    }

    @ParameterizedTest
    @MethodSource("invalidLoginRequest")
    @DisplayName("비정상 요청 시 로그인 테스트")
    void invalid_login(UserLoginRequest userLoginRequest) {
        // given
        Database.Users().put("1", new User("1", "password", "name", "email"));

        // when
        userLoginService.login(userLoginRequest);

        // then
        HttpResponse httpResponse = CustomThreadLocal.getHttpResponse();
        assertEquals(httpResponse.getStartLine().getStatusCode(), HttpStatusCode.FOUND);
        assertEquals(httpResponse.getHeader().getHeaders().get("Location"), "/user/login_failed.html");
    }

    private static Stream<Arguments> invalidLoginRequest() {
        return Stream.of(
            Arguments.of(new UserLoginRequest("2", "password")), // 존재하지 않는 유저
            Arguments.of(new UserLoginRequest("1", "password1")) // 비밀번호 불일치
        );
    }
}
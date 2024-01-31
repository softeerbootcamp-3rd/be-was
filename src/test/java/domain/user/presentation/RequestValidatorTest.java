package domain.user.presentation;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import common.http.response.HttpResponse;
import domain.user.command.application.UserCreateRequest;
import java.util.stream.Stream;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import webserver.container.CustomThreadLocal;

@DisplayName("RequestValidatorTest 클래스")
class RequestValidatorTest {

    private HttpResponse httpResponse;


    @BeforeEach
    void setUp() {
        httpResponse = new HttpResponse(null, null, null);
        CustomThreadLocal.setHttpResponse(httpResponse);
    }

    @AfterEach
    void tearDown() {
        CustomThreadLocal.clearHttpResponse();
    }

    @Test
    @DisplayName("정상 회원가입 요청 DTO 검증 테스트")
    void valid_userCreateRequestValidate() {
        // given
        UserCreateRequest userCreateRequest = new UserCreateRequest("1", "password", "name", "email");

        // when
        boolean result = RequestValidator.userCreateRequestValidate(userCreateRequest);

        // then
        assertFalse(result);
    }

    @ParameterizedTest
    @MethodSource("invalidSignupRequestProvider")
    @DisplayName("비정상 회원가입 요청 DTO 검증 테스트")
    void invalid_userCreateRequestValidate(UserCreateRequest userCreateRequest) {
        // given

        // when
        boolean result = RequestValidator.userCreateRequestValidate(userCreateRequest);

        // then
        assertTrue(result);
    }

    private static Stream<UserCreateRequest> invalidSignupRequestProvider() {
        return Stream.of(
                new UserCreateRequest(null, "password", "name", "email"),
                new UserCreateRequest("1", null, "name", "email"),
                new UserCreateRequest("1", "password", null, "email"),
                new UserCreateRequest("1", "password", "name", null)
        );
    }

    @Test
    @DisplayName("정상 로그인 요청 DTO 검증 테스트")
    void valid_userLoginRequestValidate() {
        // given
        UserLoginRequest userLoginRequest = new UserLoginRequest("1", "password");

        // when
        boolean result = RequestValidator.userLoginRequestValidate(userLoginRequest);

        // then
        assertFalse(result);
    }

    @ParameterizedTest
    @MethodSource("invalidLoginRequestProvider")
    @DisplayName("비정상 로그인 요청 DTO 검증 테스트")
    void invalid_userLoginRequestValidate(UserLoginRequest userLoginRequest) {
        // given

        // when
        boolean result = RequestValidator.userLoginRequestValidate(userLoginRequest);

        // then
        assertTrue(result);
    }

    private static Stream<UserLoginRequest> invalidLoginRequestProvider() {
        return Stream.of(
                new UserLoginRequest(null, "password"),
                new UserLoginRequest("1", null),
                new UserLoginRequest(null, null)
        );
    }
}
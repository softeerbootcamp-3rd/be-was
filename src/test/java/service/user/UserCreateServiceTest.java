package service.user;

import db.Database;
import model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("UserCreateServiceTest 클래스")
class UserCreateServiceTest {
    private UserCreateService userCreateService;

    @BeforeEach
    void setUp() {
        this.userCreateService = new UserCreateService();
        Database.findAll().clear();
        Database.addUser(new User("1", "qwer1234", "seungmin", "email1@email.com"));
    }


    @ParameterizedTest
    @MethodSource("validUserParameters")
    @DisplayName("execute 메소드는 사용자 정보가 제대로 주어지면 사용자를 생성하고 생성된 사용자 정보를 byte[]로 반환한다.")
    void create_user_success(String method, String userId, String password, String name, String email) {
        // given
        Map<String, String> params = createValidUserParams(userId, password, name, email);

        // when
        var execute = userCreateService.execute(method, params, new HashMap<>());

        // then
        assertEquals(execute.getClass(), byte[].class);
        assertEquals(2, Database.findAll().size());
    }

    private static Stream<Arguments> validUserParameters() {
        return Stream.of(
                Arguments.of("GET", "2", "qwer1234", "seungmin2", "email2@email.com")
                // 다른 유효한 테스트 케이스를 추가할 수 있음
        );
    }

    @ParameterizedTest
    @MethodSource("invalidMethods")
    @DisplayName("validate 메소드는 method 가 GET 이 아니면 IllegalArgumentException 을 던진다.")
    void invalid_http_method(String method, String userId, String password, String name, String email) {
        // given
        Map<String, String> params = createValidUserParams(userId, password, name, email);

        // when
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            userCreateService.validate(method, params, new HashMap<>());
        });

        // then
        assertEquals(IllegalArgumentException.class, exception.getClass());
    }

    @ParameterizedTest
    @MethodSource("invalidEmptyFieldParameters")
    @DisplayName("validate 메소드는 userId, password, name, email 중 하나라도 비어있으면 IllegalArgumentException 을 던진다.")
    void invalid_params(String method, String userId, String password, String name, String email) {
        // given
        Map<String, String> params = createValidUserParams(userId, password, name, email);

        // when
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            userCreateService.validate(method, params, new HashMap<>());
        });

        // then
        assertEquals(IllegalArgumentException.class, exception.getClass());
    }

    private static Stream<Arguments> invalidMethods() {
        return Stream.of(
                Arguments.of("POST", "2", "qwer1234", "seungmin2", "email2@email.com"),
                Arguments.of("PUT", "2", "qwer1234", "seungmin2", "email2@email.com"),
                Arguments.of("PATCH", "2", "qwer1234", "seungmin2", "email2@email.com"),
                Arguments.of("DELETE", "2", "qwer1234", "seungmin2", "email2@email.com")
                // 다른 유효하지 않은 테스트 케이스를 추가할 수 있음
        );
    }

    private static Stream<Arguments> invalidEmptyFieldParameters() {
        return Stream.of(
                Arguments.of("POST", "2", "qwer1234", "seungmin2", "email2@email.com"),  // method 가 GET 이 아닌 경우
                Arguments.of("GET", "", "qwer1234", "seungmin2", "email2@email.com"),  // userId 가 비어있는 경우
                Arguments.of("GET", "2", "", "seungmin2", "email2@email.com"),  // password 가 비어있는 경우
                Arguments.of("GET", "2", "qwer1234", "", "email2@email.com"),  // name 이 비어있는 경우
                Arguments.of("GET", "2", "qwer1234", "seungmin2", "")  // email 이 비어있는 경우
                // 다른 유효하지 않은 테스트 케이스를 추가할 수 있음
        );
    }

    private Map<String, String> createValidUserParams(String userId, String password, String name, String email) {
        return Map.of(
                "userId", userId,
                "password", password,
                "name", name,
                "email", email
        );
    }
}

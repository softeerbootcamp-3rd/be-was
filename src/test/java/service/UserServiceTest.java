package service;

import db.Database;
import model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@DisplayName("UserServiceTest 클래스")
class UserServiceTest {

    private UserService userService;
    private Database database;

    @BeforeEach
    void init() {
        this.userService = UserService.getInstance();
        this.database = new Database();
    }

    @ParameterizedTest
    @MethodSource("validUserParameters")
    @DisplayName("회원가입이 정상적으로 처리되는지 확인")
    void sign_up_success(String request) {

        // when
        userService.signUp(request);
        User findUser = database.findUserById("test2");

        // then
        assertThat(findUser).isNotNull();
    }

    @ParameterizedTest
    @MethodSource("invalidUserParameters")
    @DisplayName("회원가입 진행 중 매개변수가 유효하지 않으면 예외를 IllegalArgumentException 예외를 발생시키는지 확인")
    void invalid_param(String request) {

        assertThatThrownBy(() -> userService.signUp(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("회원가입에 실패하였습니다.");
    }

    @ParameterizedTest
    @MethodSource("invalidMethodParameters")
    @DisplayName("회원가입 진행 중 요청 메소드가 GET이 아니면 예외를 IllegalArgumentException 예외를 발생시키는지 확인")
    void invalid_method(String request) {

        assertThatThrownBy(() -> userService.signUp(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("회원가입에 실패하였습니다.");
    }


    private static Stream<Arguments> validUserParameters() {
        return Stream.of(
                Arguments.of("GET userId=test2&password=test2&name=test2&email=test2@test.com HTTP/1.1")
        );
    }

    private static Stream<Arguments> invalidUserParameters() {
        return Stream.of(
                Arguments.of("GET userId=test2&password=test2&name=test2 HTTP/1.1"), // email이 없는 경우
                Arguments.of("GET password=test2&name=test2&email=test2@test.com HTTP/1.1"), // userId가 없는 경우
                Arguments.of("GET userId=test2&name=test2&email=test2@test.com HTTP/1.1"), // password가 없는 경우
                Arguments.of("GET userId=test2^password=test2&email=test2@test.com HTTP/1.1") // name이 없는 경우
        );
    }

    private static Stream<Arguments> invalidMethodParameters() {
        return Stream.of(
                Arguments.of("POST userId=test2&password=test2&name=test2&email=test2@test.com HTTP/1.1"), // POST 요청인 경우
                Arguments.of("PUT userId=test2&password=test2&name=test2&email=test2@test.com HTTP/1.1") // PUT 요청인 경우
        );
    }
}

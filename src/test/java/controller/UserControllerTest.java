package controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DisplayName("UserControllerTest 클래스")
class UserControllerTest {

    private UserController userController;

    @BeforeEach
    void init() {
        this.userController = UserController.getInstance();
    }

    @ParameterizedTest
    @MethodSource("status200Parameters")
    @DisplayName("200 상태코드를 반환하는지 확인")
    void return_status_200(String requestLine) {

        // when
        int status = userController.route(requestLine).getStatus();

        // then
        assertThat(status).isEqualTo(200);
    }

    @ParameterizedTest
    @MethodSource("status302Parameters")
    @DisplayName("302 상태코드를 반환하는지 확인")
    void return_status_302(String requestLine) {

        // when
        int status = userController.route(requestLine).getStatus();

        // then
        assertThat(status).isEqualTo(302);
    }

    @ParameterizedTest
    @MethodSource("status404Parameters")
    @DisplayName("404 상태코드를 반환하는지 확인")
    void return_status_404(String requestLine) {

        // when
        int status = userController.route(requestLine).getStatus();

        // then
        assertThat(status).isEqualTo(404);
    }


    private static Stream<Arguments> status200Parameters() {
        return Stream.of(
                Arguments.of("GET /user/form.html HTTP/1.1"),
                Arguments.of("GET /user/login.html HTTP/1.1"),
                Arguments.of("GET /user/login_failed.html HTTP/1.1"),
                Arguments.of("GET /user/profile.html HTTP/1.1"),
                Arguments.of("GET /user/list.html HTTP/1.1")
        );
    }

    private static Stream<Arguments> status302Parameters() {
        return Stream.of(
                Arguments.of("GET /user/create?userId=test1&password=password&name=이름&email=test1%40test.com HTTP/1.1"),
                Arguments.of("GET /user/create?userId=test2&password=password&name=이름&email=test2%40test.com HTTP/1.1"),
                Arguments.of("GET /user/create?userId=test3&password=password&name=이름&email=test3%40test.com HTTP/1.1")
        );
    }

    private static Stream<Arguments> status404Parameters() {
        return Stream.of(
                Arguments.of("GET /qna HTTP/1.1"),
                Arguments.of("GET /qna/form.html HTTP/1.1"),
                Arguments.of("GET /qna/show.html HTTP/1.1")
        );
    }
}
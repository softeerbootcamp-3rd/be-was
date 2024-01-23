package controller;

import db.Database;
import model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import request.HttpRequest;
import response.HttpResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DisplayName("UserControllerTest 클래스")
class UserControllerTest {

    private UserController userController;
    private Database database;

    @BeforeEach
    void init() {
        this.userController = UserController.getInstance();
        this.database = new Database();
    }

    @ParameterizedTest
    @MethodSource("status_200_Parameters")
    @DisplayName("요청 URI에 해당하는 파일이 존재하면 200 상태코드를 반환하는지 확인")
    void return_status_200(HttpRequest httpRequest) throws Exception {

        // when
        HttpResponse httpResponse = userController.handleUserRequest(httpRequest);

        // then
        assertThat(httpResponse.getStatusCode()).isEqualTo(200);
    }

    @ParameterizedTest
    @MethodSource("status_302_Parameters")
    @DisplayName("302 상태코드를 반환하고 회원가입이 정상적으로 처리되는지 확인")
    void return_status_302(HttpRequest httpRequest) throws Exception {

        // when
        HttpResponse httpResponse = userController.handleUserRequest(httpRequest);
        User findUser = database.findUserById("test");

        // then
        assertThat(findUser).isNotNull()
                .extracting("userId", "password", "name", "email")
                .contains(findUser.getUserId(), findUser.getPassword(), findUser.getName(), findUser.getEmail());
        assertThat(httpResponse.getStatusCode()).isEqualTo(302);
    }

    @ParameterizedTest
    @MethodSource("status_404_Parameters")
    @DisplayName("요청 URI에 해당하는 파일이 존재하지 않으면 404 상태코드를 반환하는지 확인")
    void return_status_404(HttpRequest httpRequest) throws Exception {

        // when
        HttpResponse httpResponse = userController.handleUserRequest(httpRequest);

        // then
        assertThat(httpResponse.getStatusCode()).isEqualTo(404);
    }

    private static Stream<Arguments> status_200_Parameters() throws IOException {
        return Stream.of(
                Arguments.of(new HttpRequest(new BufferedReader(new StringReader("GET /user/form.html HTTP/1.1"
                        + "\r\n"
                        + "Content-Type: application/x-www-form-urlencoded"
                        + "\r\n"
                        + "\r\n")))),
                Arguments.of(new HttpRequest(new BufferedReader(new StringReader("GET /user/login.html HTTP/1.1"
                        + "\r\n"
                        + "Content-Type: application/x-www-form-urlencoded"
                        + "\r\n"
                        + "\r\n"))))
        );
    }

    private static Stream<Arguments> status_302_Parameters() throws IOException {
        return Stream.of(
                Arguments.of(new HttpRequest(new BufferedReader(new StringReader("POST /user/create HTTP/1.1"
                        + "\r\n"
                        + "Content-Length: 59"
                        + "\r\n"
                        + "Content-Type: application/x-www-form-urlencoded"
                        + "\r\n"
                        + "\r\n"
                        + "userId=test&password=test&name=test&email=test@test.com"))))
        );
    }


    private static Stream<Arguments> status_404_Parameters() throws IOException {
        return Stream.of(
                Arguments.of(new HttpRequest(new BufferedReader(new StringReader("GET /user/forms.html HTTP/1.1"
                        + "\r\n"
                        + "Content-Type: application/x-www-form-urlencoded"
                        + "\r\n"
                        + "\r\n")))),
                Arguments.of(new HttpRequest(new BufferedReader(new StringReader("GET /user/logins.html HTTP/1.1"
                        + "\r\n"
                        + "Content-Type: application/x-www-form-urlencoded"
                        + "\r\n"
                        + "\r\n")))),
                Arguments.of(new HttpRequest(new BufferedReader(new StringReader("GET /user/create HTTP/1.1"
                        + "\r\n"
                        + "Content-Type: application/x-www-form-urlencoded"
                        + "\r\n"
                        + "\r\n"))))
        );
    }
}

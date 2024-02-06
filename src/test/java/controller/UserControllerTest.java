package controller;

import db.Database;
import model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import request.http.HttpRequest;
import response.http.HttpResponse;

import java.io.*;
import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static util.StatusCode.*;

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
        assertThat(httpResponse.getStatusCode()).isEqualTo(OK.getStatus());
    }

    @ParameterizedTest
    @MethodSource("status_302_Parameters")
    @DisplayName("302 상태코드를 반환하고 회원가입이 정상적으로 처리되는지 확인")
    void return_status_302(HttpRequest httpRequest) throws Exception {

        // when
        HttpResponse httpResponse = userController.handleUserRequest(httpRequest);
        User findUser = database.findUserById("taegon1998");

        // then
        assertThat(findUser).isNotNull()
                .extracting("userId", "password", "name", "email")
                .contains(findUser.getUserId(), findUser.getPassword(), findUser.getName(), findUser.getEmail());
        assertThat(httpResponse.getStatusCode()).isEqualTo(FOUND.getStatus());
    }

    @ParameterizedTest
    @MethodSource("status_404_Parameters")
    @DisplayName("요청 URI에 해당하는 파일이 존재하지 않으면 404 상태코드를 반환하는지 확인")
    void return_status_404(HttpRequest httpRequest) throws Exception {

        // when
        HttpResponse httpResponse = userController.handleUserRequest(httpRequest);

        // then
        assertThat(httpResponse.getStatusCode()).isEqualTo(NOT_FOUND.getStatus());
    }

    @ParameterizedTest
    @MethodSource("login_success_parameters")
    @DisplayName("database에 존재하는 회원이면 sid값을 생성하고 302 상태코드를 반환하는지 확인")
    void login_success(HttpRequest httpRequest) throws Exception {

        // when
        HttpResponse httpResponse = userController.handleUserRequest(httpRequest);

        // then
        assertThat(httpResponse.getSid());
        assertThat(httpResponse.getStatusCode()).isEqualTo(FOUND.getStatus());
    }

    @ParameterizedTest
    @MethodSource("login_fail_parameters")
    @DisplayName("database에 회원정보가 존재하지 않거나 비밀번호가 틀리면 sid값을 생성하지 않고 302 상태코드를 반환하는지 확인")
    void login_fail(HttpRequest httpRequest) throws Exception {

        // when
        HttpResponse httpResponse = userController.handleUserRequest(httpRequest);

        // then
        assertThat(httpResponse.getSid()).isNull();
        assertThat(httpResponse.getStatusCode()).isEqualTo(FOUND.getStatus());
    }

    @ParameterizedTest
    @MethodSource("status_404_when_not_post_method_parameters")
    @DisplayName("회원가입을 요청할 때, POST 메소드가 아니면 404 상태코드를 반환하는지 확인")
    void return_status_404_when_user_create(HttpRequest httpRequest) throws Exception {

        // when
        HttpResponse httpResponse = userController.handleUserRequest(httpRequest);

        // then
        assertThat(httpResponse.getStatusCode()).isEqualTo(NOT_FOUND.getStatus());
    }


    private static Stream<Arguments> status_200_Parameters() throws IOException {
        String httpRequestString = "GET /user/login.html HTTP/1.1\r\n"
                + "Content-Type: application/x-www-form-urlencoded\r\n"
                + "\r\n";
        InputStream inputStream = new ByteArrayInputStream(httpRequestString.getBytes("ISO-8859-1"));

        return Stream.of(
                Arguments.of(new HttpRequest(inputStream))
        );
    }

    private static Stream<Arguments> status_302_Parameters() throws IOException {
        String httpRequestString = "POST /user/create HTTP/1.1\r\n"
                + "Content-Length: 27\r\n"
                + "Content-Type: application/x-www-form-urlencoded\r\n"
                + "\r\n";
        InputStream inputStream = new ByteArrayInputStream(httpRequestString.getBytes("ISO-8859-1"));
        return Stream.of(
                Arguments.of(new HttpRequest(inputStream))
        );
    }


    private static Stream<Arguments> status_404_Parameters() throws IOException {
        String httpRequestString = "GET /user/list HTTP/1.1\r\n"
                + "Content-Type: application/x-www-form-urlencoded\r\n"
                + "\r\n";
        InputStream inputStream = new ByteArrayInputStream(httpRequestString.getBytes("ISO-8859-1"));
        return Stream.of(
                Arguments.of(new HttpRequest(inputStream))
        );
    }

    private static Stream<Arguments> login_success_parameters() throws IOException {
        String httpRequestString = "POST /user/login HTTP/1.1\r\n"
                + "Content-Length: 27\r\n"
                + "Content-Type: application/x-www-form-urlencoded\r\n"
                + "\r\n"
                + "userId=test1&password=test1";
        InputStream inputStream = new ByteArrayInputStream(httpRequestString.getBytes("ISO-8859-1"));
        return Stream.of(
                Arguments.of(new HttpRequest(inputStream))
        );
    }

    private static Stream<Arguments> login_fail_parameters() throws IOException {
        String httpRequestString = "POST /user/login HTTP/1.1\r\n"
                + "Content-Length: 27\r\n"
                + "Content-Type: application/x-www-form-urlencoded\r\n"
                + "\r\n"
                + "userId=test1&password=test2";
        InputStream inputStream = new ByteArrayInputStream(httpRequestString.getBytes("ISO-8859-1"));
        return Stream.of(
                Arguments.of(new HttpRequest(inputStream))
        );
    }

    private static Stream<Arguments> status_404_when_not_post_method_parameters() throws IOException {
        String httpRequestString = "GET /user/create HTTP/1.1\r\n"
                + "Content-Length: 27\r\n"
                + "Content-Type: application/x-www-form-urlencoded\r\n"
                + "\r\n"
                + "userId=test1&password=test2";
        InputStream inputStream = new ByteArrayInputStream(httpRequestString.getBytes("ISO-8859-1"));
        return Stream.of(
                Arguments.of(new HttpRequest(inputStream))
        );
    }
}

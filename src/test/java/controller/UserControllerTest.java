package controller;

import db.Database;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import webserver.http.HttpRequest;
import webserver.http.HttpResponse;
import webserver.http.HttpStatus;
import model.User;
import org.junit.jupiter.api.Test;
import webserver.http.SessionManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

import static org.assertj.core.api.Assertions.assertThat;

class UserControllerTest {
    @BeforeAll
    static void init() {
        Database.addUser(new User("abc", "abc", "abc", "abc@email.com"));
        Database.addUser(new User("jin", "jin", "jin", "jin@email.com"));
    }

    @Test
    @DisplayName("POST 요청에 대한 회원 가입 테스트")
    void signupTest() throws IOException {
        UserController userController = UserController.getInstance();
        HttpRequest request = HttpRequest.createFromReader(new BufferedReader(
                new StringReader("POST /user/create HTTP/1.1\r\n" +
                        "Content-Length: 58\r\n" +
                        "\r\n" +
                        "userId=asdf&password=asdf&name=asdf&email=asdf%40gmail.com")
        ));
        HttpResponse response = HttpResponse.of();

        userController.signup(request, response);

        assertThat(response.getStatusCode())
                .isEqualTo(HttpStatus.FOUND);

        assertThat(response.getHeaderProperty("Location"))
                .isEqualTo("/index.html");

        User user = Database.findUserById("asdf");
        assertThat(user).isNotNull();
        assertThat(user.getEmail()).isEqualTo("asdf@gmail.com");
    }

    @Test
    @DisplayName("POST 요청에 대한 로그인 성공 테스트")
    void loginSuccessTest() throws IOException {
        UserController userController = UserController.getInstance();
        HttpRequest request = HttpRequest.createFromReader(new BufferedReader(
                new StringReader("POST /user/login HTTP/1.1\r\n" +
                        "Content-Length: 23\r\n" +
                        "\r\n" +
                        "userId=jin&password=jin")
        ));
        HttpResponse response = HttpResponse.of();

        userController.login(request, response);

        assertThat(response.getStatusCode())
                .isEqualTo(HttpStatus.FOUND);
        assertThat(response.getHeaderProperty("Location"))
                .isEqualTo("/index.html");
        assertThat(response.getHeaderProperty("Set-Cookie"))
                .isNotBlank();
    }

    @Test
    @DisplayName("POST 요청에 대한 로그인 실패 테스트")
    void loginFailTest() throws IOException {
        UserController userController = UserController.getInstance();
        HttpRequest request = HttpRequest.createFromReader(new BufferedReader(
                new StringReader("POST /user/login HTTP/1.1\r\n" +
                        "Content-Length: 31\r\n" +
                        "\r\n" +
                        "userId=invalid&password=invalid")
        ));
        HttpResponse response = HttpResponse.of();

        userController.login(request, response);

        assertThat(response.getStatusCode())
                .isEqualTo(HttpStatus.FOUND);
        assertThat(response.getHeaderProperty("Location"))
                .isEqualTo("/user/login_failed.html");
        assertThat(response.getHeaderProperty("Set-Cookie"))
                .isBlank();
    }

    @Test
    @DisplayName("GET /user/list에 대한 유저 목록 나열 성공 테스트")
    void listUsersSuccessTest() throws IOException {
        String sid = SessionManager.createSession(
                new User("mock", "mock", "mock", "mock@mock.com"));
        UserController userController = UserController.getInstance();
        HttpRequest request = HttpRequest.createFromReader(new BufferedReader(
                new StringReader("GET /user/list HTTP/1.1\n" +
                        "Cookie: sid=" + sid + "\n" +
                        "\n"
                )
        ));
        HttpResponse response = HttpResponse.of();

        userController.list(request, response);

        String bodyString = new String(response.getBody());

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getHeaderProperty("Location")).isBlank();
        assertThat(bodyString).contains("<th>사용자 아이디</th>");
        assertThat(bodyString).contains("<th>이름</th>");
        assertThat(bodyString).contains("<th>이메일</th>");
    }

    @Test
    @DisplayName("GET /user/list에 대한 유저 목록 나열 실패 테스트")
    void listUsersFailTest() throws IOException {
        UserController userController = UserController.getInstance();
        HttpRequest request = HttpRequest.createFromReader(new BufferedReader(
                new StringReader("GET /user/list HTTP/1.1\n"
                        + "\n")
        ));
        HttpResponse response = HttpResponse.of();

        userController.list(request, response);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FOUND);
        assertThat(response.getHeaderProperty("Location")).isEqualTo("/user/login.html");
    }
}

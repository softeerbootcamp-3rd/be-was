package controller;

import db.SessionDatabase;
import db.UserDatabase;
import dto.HttpRequestDto;
import dto.HttpRequestDtoBuilder;
import dto.HttpResponseDto;
import model.Session;
import model.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import service.UserService;

public class UserControllerTest {
    private final UserService userService = new UserService();
    private final UserController userController = new UserController(userService);

    @Test
    @DisplayName("createUser(): 유저 생성이 성공한 경우 /index.html로 리다이렉트하는 응답을 리턴한다")
    public void createUserTest() {
        // given
        HttpRequestDto request = new HttpRequestDtoBuilder("POST", "/user/create", "HTTP/1.1")
                .setBody("userId=testUserId&password=testPassword&name=testName&email=test%40softeer.com")
                .build();

        // when
        HttpResponseDto httpResponseDto = userController.handleRequest(request);

        // then
        Assertions.assertThat(httpResponseDto.getStatus()).isEqualTo("302");
        Assertions.assertThat(httpResponseDto.getMessage()).isEqualTo("Found");
        Assertions.assertThat(httpResponseDto.getHeaders().get("Location")).isEqualTo("/index.html");
    }

    @Test
    @DisplayName("createUser(): 필요한 파라미터를 모두 입력하지 않아 유저 생성이 실패한 경우 400 Bad Request 응답을 리턴한다")
    public void createUserFailInvalidParamsTest() {
        // given
        HttpRequestDto request = new HttpRequestDtoBuilder("POST", "/user/create", "HTTP/1.1")
                .setBody("userId=testUserId&password=testPassword&name=testName")
                .build();

        // when
        HttpResponseDto httpResponseDto = userController.handleRequest(request);

        // then
        Assertions.assertThat(httpResponseDto.getStatus()).isEqualTo("400");
        Assertions.assertThat(httpResponseDto.getMessage()).isEqualTo("Bad Request");
    }

    @Test
    @DisplayName("createUser(): Response Body를 입력하지 않아 유저 생성이 실패한 경우 400 Bad Request 응답을 리턴한다")
    public void createUserFailRequireBodyTest() {
        // given
        HttpRequestDto request = new HttpRequestDtoBuilder("POST", "/user/create", "HTTP/1.1").build();

        // when
        HttpResponseDto httpResponseDto = userController.handleRequest(request);

        // then
        Assertions.assertThat(httpResponseDto.getStatus()).isEqualTo("400");
        Assertions.assertThat(httpResponseDto.getMessage()).isEqualTo("Bad Request");
    }

    @Test
    @DisplayName("loginUser(): 유효한 기존 세션 정보가 없는 유저가 로그인에 성공한 경우 /index.html로 리다이렉트하는 응답을 새로운 sessionId와 함께 리턴한다")
    public void loginNewUserTest() {
        // given
        UserDatabase.addUser(new User("testUserId", "testPassword", "testName", "test@example.com"));

        HttpRequestDto request = new HttpRequestDtoBuilder("POST", "/user/login", "HTTP/1.1")
                .setBody("userId=testUserId&password=testPassword")
                .build();

        // when
        HttpResponseDto httpResponseDto = userController.handleRequest(request);

        // then
        Assertions.assertThat(httpResponseDto.getStatus()).isEqualTo("302");
        Assertions.assertThat(httpResponseDto.getMessage()).isEqualTo("Found");
        Assertions.assertThat(httpResponseDto.getHeaders().get("Location")).isEqualTo("/index.html");

        Session session = SessionDatabase.findValidSessionByUserId("testUserId");
        Assertions.assertThat(httpResponseDto.getHeaders().get("Set-Cookie")).isEqualTo("sid=" + session.getSessionId() + "; Path=/");
    }

    @Test
    @DisplayName("loginUser(): 유효한 기존 세션 정보가 있는 유저가 로그인에 성공한 경우 /index.html로 리다이렉트하는 응답을 기존에 존재하는 sessionId와 함께 리턴한다")
    public void loginUserTest() {
        // given
        UserDatabase.addUser(new User("testUserId", "testPassword", "testName", "test@example.com"));
        String sessionId = SessionDatabase.addSession(new Session("testUserId"));

        HttpRequestDto request = new HttpRequestDtoBuilder("POST", "/user/login", "HTTP/1.1")
                .setBody("userId=testUserId&password=testPassword")
                .build();

        // when
        HttpResponseDto httpResponseDto = userController.handleRequest(request);

        // then
        Assertions.assertThat(httpResponseDto.getStatus()).isEqualTo("302");
        Assertions.assertThat(httpResponseDto.getMessage()).isEqualTo("Found");
        Assertions.assertThat(httpResponseDto.getHeaders().get("Location")).isEqualTo("/index.html");
        Assertions.assertThat(httpResponseDto.getHeaders().get("Set-Cookie")).isEqualTo("sid=" + sessionId + "; Path=/");
    }

    @Test
    @DisplayName("loginUser(): 필요한 파라미터를 모두 입력하지 않아 로그인에 실패한 경우 /user/login_failed.html로 리다이렉트하는 응답을 리턴한다")
    public void loginUserFailInvalidParamsTest() {
        // given
        HttpRequestDto request = new HttpRequestDtoBuilder("POST", "/user/login", "HTTP/1.1")
                .setBody("userId=testUserId")
                .build();

        // when
        HttpResponseDto httpResponseDto = userController.handleRequest(request);

        // then
        Assertions.assertThat(httpResponseDto.getStatus()).isEqualTo("302");
        Assertions.assertThat(httpResponseDto.getMessage()).isEqualTo("Found");
        Assertions.assertThat(httpResponseDto.getHeaders().get("Location")).isEqualTo("/user/login_failed.html");
    }

    @Test
    @DisplayName("loginUser(): 유효하지 않은 정보를 입력해서 로그인에 실패한 경우 /user/login_failed.html로 리다이렉트하는 응답을 리턴한다")
    public void loginUserFailInvalidInfoTest() {
        // given
        UserDatabase.addUser(new User("testUserId", "testPassword", "testName", "test@example.com"));
        HttpRequestDto request = new HttpRequestDtoBuilder("POST", "/user/login", "HTTP/1.1")
                .setBody("userId=testUserId&password=wrongPassword")
                .build();

        // when
        HttpResponseDto httpResponseDto = userController.handleRequest(request);

        // then
        Assertions.assertThat(httpResponseDto.getStatus()).isEqualTo("302");
        Assertions.assertThat(httpResponseDto.getMessage()).isEqualTo("Found");
        Assertions.assertThat(httpResponseDto.getHeaders().get("Location")).isEqualTo("/user/login_failed.html");
    }

    @Test
    @DisplayName("loginUser(): Response Body를 입력하지 않아 로그인이 실패한 경우 /user/login_failed.html로 리다이렉트하는 응답을 리턴한다")
    public void loginUserFailRequireBodyTest() {
        // given
        HttpRequestDto request = new HttpRequestDtoBuilder("POST", "/user/login", "HTTP/1.1").build();

        // when
        HttpResponseDto httpResponseDto = userController.handleRequest(request);

        // then
        Assertions.assertThat(httpResponseDto.getStatus()).isEqualTo("302");
        Assertions.assertThat(httpResponseDto.getMessage()).isEqualTo("Found");
        Assertions.assertThat(httpResponseDto.getHeaders().get("Location")).isEqualTo("/user/login_failed.html");
    }
}

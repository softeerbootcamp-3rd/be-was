package controller;

import db.Database;
import model.HttpRequest;
import model.HttpResponse;
import model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.assertj.core.api.Assertions.assertThat;

class UserControllerTest {

    @BeforeEach
    void setUp() {
        User existingUser = new User("userId", "1234", "name", "test@test.com");
        Database.addUser(existingUser);
    }

    @DisplayName("회원가입을 성공하면 redirect response를 보낸다.")
    @Test
    void userController() throws IOException {
        //given
        HttpRequest mockRequest = createMockRequest("user1", "1234", "name", "soeun@naver.com", "64");

        //when
        HttpResponse httpResponse = UserController.createUser(mockRequest);

        //then
        String responseToString = httpResponse.toString();
        assertThat(responseToString).isEqualTo("HttpResponse{httpStatus=FOUND, body=}");
    }

    @DisplayName("중복 아이디인 경우 ErrorResponse를 보낸다.")
    @Test
    void userControllerSameUserId() throws IOException {
        //given
        HttpRequest mockRequest = createMockRequest("userId", "1234", "name", "soeun@naver.com", "64");

        //when
        HttpResponse httpResponse = UserController.createUser(mockRequest);

        //then
        String responseToString = httpResponse.toString();
        assertThat(responseToString).isEqualTo("HttpResponse{httpStatus=BAD_REQUEST, body=이미 존재하는 아이디입니다.}");
    }

    @DisplayName("회원 가입 시 아이디가 없는 경우 ErrorResponse를 보낸다.")
    @Test
    void userControllerSameUserIdWithoutUserId() throws IOException {
        //given
        HttpRequest mockRequest = createMockRequest("", "1234", "name", "soeun@naver.com", "82");

        //when
        HttpResponse httpResponse = UserController.createUser(mockRequest);

        //then
        String responseToString = httpResponse.toString();
        assertThat(responseToString).isEqualTo("HttpResponse{httpStatus=BAD_REQUEST, body=userId는 필수입니다.}");
    }

    private static HttpRequest createMockRequest(String userId, String password, String name, String email, String contentLength) throws IOException {
        String mockRequest = "POST /user/create HTTP/1.1\n" +
                "Host: localhost:8080\n" +
                "Connection: keep-alive\n" +
                "Accept: */*\n" +
                "Content-Length: " + contentLength + "\n\n" +
                "userId=" + userId + "&password=" + password + "&name=" + name + "&email=" + email;

        InputStream in = new ByteArrayInputStream(mockRequest.getBytes());

        return HttpRequest.from(in);
    }
}

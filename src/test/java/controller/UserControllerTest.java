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
        HttpRequest mockRequest = createMockRequest("GET /user/create?userId=user1&password=1234&name=test&email=test%40naver.com HTTP/1.1");

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
        HttpRequest mockRequest = createMockRequest("GET /user/create?userId=userId&password=1234&name=name&email=test%40naver.com HTTP/1.1");

        //when
        HttpResponse httpResponse = UserController.createUser(mockRequest);

        //then
        String responseToString = httpResponse.toString();
        assertThat(responseToString).isEqualTo("HttpResponse{httpStatus=BAD_REQUEST, body=이미 존재하는 아이디입니다.}");
    }

    private static HttpRequest createMockRequest(String requestLine) throws IOException {
        String mockRequest = requestLine + "\n" +
                "Host: localhost:8080\n" +
                "Connection: keep-alive\n" +
                "Accept: */*\n" +
                "Content-Length: 23\n\n" +
                "request body";

        InputStream in = new ByteArrayInputStream(mockRequest.getBytes());

        return HttpRequest.from(in);
    }

}
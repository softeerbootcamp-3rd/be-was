package controller;

import dto.HttpRequestDto;
import dto.HttpRequestDtoBuilder;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;

public class UserControllerTest {
    private final UserController userController = new UserController();

    @Test()
    @DisplayName("UserController.handleRequest() Test")
    public void handleRequestTest() {
        // given
        HttpRequestDto httpRequestDto = new HttpRequestDtoBuilder("GET", "/user/form.html", "HTTP/1.1").build();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(byteArrayOutputStream);

        // when
        userController.handleRequest(httpRequestDto, dos);

        // then
        String response = byteArrayOutputStream.toString();
        System.out.println(response);
        Assertions.assertThat(response).contains("HTTP/1.1 200 OK");
    }

    @Test()
    @DisplayName("UserController.handleRequest() Test - createUser")
    public void createUserTest() {
        // given
        HttpRequestDto httpRequestDto = new HttpRequestDtoBuilder("GET", "/user/create?userId=userId&password=password&name=name&email=softeer@example.com", "HTTP/1.1").build();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(byteArrayOutputStream);

        // when
        userController.handleRequest(httpRequestDto, dos);

        // then
        String response = byteArrayOutputStream.toString();
        System.out.println(response);
        Assertions.assertThat(response).contains("HTTP/1.1 302 Found");
    }

    @Test()
    @DisplayName("UserController.handleRequest() Test - creatUser Fail")
    public void createUserFailTest() {
        // given
        HttpRequestDto httpRequestDto = new HttpRequestDtoBuilder("GET", "/user/create?userId=userId&password=password", "HTTP/1.1").build();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(byteArrayOutputStream);

        // when
        userController.handleRequest(httpRequestDto, dos);

        // then
        String response = byteArrayOutputStream.toString();
        System.out.println(response);
        Assertions.assertThat(response).contains("HTTP/1.1 400 Bad Request");
    }
}

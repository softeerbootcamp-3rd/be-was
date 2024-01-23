package controller;

import dto.HttpRequestDto;
import dto.HttpRequestDtoBuilder;
import dto.HttpResponseDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import service.UserService;
import util.WebUtil;

import java.util.Collections;

public class UserControllerTest {
    private final UserService userService = Mockito.mock(UserService.class);

    private final UserController userController = new UserController(userService);

    @Test
    @DisplayName("createUser(): 유저 생성이 성공한 경우 /index.html로 리다이렉트하는 응답을 리턴한다")
    public void createUserTest() {
        // given
        HttpRequestDto request = new HttpRequestDtoBuilder("POST", "/user/create", "HTTP/1.1").build();
        Mockito.doNothing().when(userService).createUser(Mockito.any());

        // when
        HttpResponseDto httpResponseDto;
        try (MockedStatic<WebUtil> webUtil = Mockito.mockStatic(WebUtil.class)) {
            webUtil.when(() -> WebUtil.parseRequestBody(Mockito.any()))
                            .thenReturn(Collections.emptyMap());
            httpResponseDto = userController.handleRequest(request);
        }

        // then
        Assertions.assertThat(httpResponseDto.getStatus()).isEqualTo("302");
        Assertions.assertThat(httpResponseDto.getMessage()).isEqualTo("Found");
        Assertions.assertThat(httpResponseDto.getHeaders().get("Location")).isEqualTo("/index.html");
    }

    @Test
    @DisplayName("createUser(): 유저 생성이 실패한 경우 400 Bad Request 응답을 리턴한다")
    public void createUserFailTest() {
        // given
        HttpRequestDto request = new HttpRequestDtoBuilder("POST", "/user/create", "HTTP/1.1").build();
        Mockito.doThrow(new IllegalArgumentException("Invalid Parameters")).when(userService).createUser(Mockito.any());

        // when
        HttpResponseDto httpResponseDto;
        try (MockedStatic<WebUtil> webUtil = Mockito.mockStatic(WebUtil.class)) {
            webUtil.when(() -> WebUtil.parseRequestBody(Mockito.any()))
                    .thenReturn(Collections.emptyMap());
            httpResponseDto = userController.handleRequest(request);
        }

        // then
        Assertions.assertThat(httpResponseDto.getStatus()).isEqualTo("400");
    }

    @Test
    @DisplayName("loginUser(): 로그인이 성공한 경우 /index.html로 리다이렉트하는 응답을 sessionId와 함께 리턴한다")
    public void loginUserTest() {
        // given
        HttpRequestDto request = new HttpRequestDtoBuilder("POST", "/user/login", "HTTP/1.1").build();
        Mockito.doReturn("sessionId").when(userService).loginUser(Mockito.any());

        // when
        HttpResponseDto httpResponseDto;
        try (MockedStatic<WebUtil> webUtil = Mockito.mockStatic(WebUtil.class)) {
            webUtil.when(() -> WebUtil.parseRequestBody(Mockito.any()))
                    .thenReturn(Collections.emptyMap());
            httpResponseDto = userController.handleRequest(request);
        }

        // then
        Assertions.assertThat(httpResponseDto.getStatus()).isEqualTo("302");
        Assertions.assertThat(httpResponseDto.getMessage()).isEqualTo("Found");
        Assertions.assertThat(httpResponseDto.getHeaders().get("Location")).isEqualTo("/index.html");
        Assertions.assertThat(httpResponseDto.getHeaders().get("Set-Cookie")).isEqualTo("sid=sessionId; Path=/");
    }

    @Test
    @DisplayName("loginUser(): 로그인이 실패한 경우 /user/login_failed.html로 리다이렉트하는 응답을 리턴한다")
    public void loginUserFailTest() {
        // given
        HttpRequestDto request = new HttpRequestDtoBuilder("POST", "/user/login", "HTTP/1.1").build();
        Mockito.doThrow(new IllegalArgumentException("Invalid Parameters")).when(userService).loginUser(Mockito.any());

        // when
        HttpResponseDto httpResponseDto;
        try (MockedStatic<WebUtil> webUtil = Mockito.mockStatic(WebUtil.class)) {
            webUtil.when(() -> WebUtil.parseRequestBody(Mockito.any()))
                    .thenReturn(Collections.emptyMap());
            httpResponseDto = userController.handleRequest(request);
        }

        // then
        Assertions.assertThat(httpResponseDto.getStatus()).isEqualTo("302");
        Assertions.assertThat(httpResponseDto.getMessage()).isEqualTo("Found");
        Assertions.assertThat(httpResponseDto.getHeaders().get("Location")).isEqualTo("/user/login_failed.html");
    }
}

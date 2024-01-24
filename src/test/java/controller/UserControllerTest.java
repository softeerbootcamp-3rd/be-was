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
    @DisplayName("createUser() Test : Success case - UserService.createUser()가 성공한 경우")
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
    @DisplayName("createUser() Test : Fail case - UserService가 IllegalArgumentException 던진 경우")
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
}

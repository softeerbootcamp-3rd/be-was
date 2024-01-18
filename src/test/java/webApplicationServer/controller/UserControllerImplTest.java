package webApplicationServer.controller;

import controller.UserController;
import controller.UserControllerImpl;
import dto.HttpResponseDto;
import dto.UserSignUpDto;
import exception.BadRequestException;
import model.http.Status;
import model.http.request.HttpRequest;
import model.http.request.StartLine;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import service.UserService;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class UserControllerImplTest {
    private UserController userController;
    private UserService userServiceMock;
    @BeforeEach
    public void setUP(){
        userServiceMock = mock(UserService.class);
        userController = new UserControllerImpl(userServiceMock);
    }

    @Test
    @DisplayName("UserController 가 Singleton으로 동일한 클래스를 반환한다.")
    void testUserControllerSingleton() {
        UserController userController1 = UserControllerImpl.getInstance();
        UserController userController2 = UserControllerImpl.getInstance();
        assertThat(userController1).isSameAs(userController2);
    }

    @Test
    @DisplayName("url이 /user/create 로 시작할때 회원가입을 진행하고 " +
            "ResponseHeader를 Status : REDIRECT, Location : /user/login.html로  설정한다.")
    void UserControllerCallUserServiceAndSetResponseRedirectToLogin() {
        //given
        HttpRequest httpRequestMock = mock(HttpRequest.class);
        HttpResponseDto httpResponseMock = mock(HttpResponseDto.class);
        StartLine startLineMock = mock(StartLine.class);

        when(httpRequestMock.getStartLine()).thenReturn(startLineMock);
        when(httpRequestMock.getStartLine().getPathUrl()).thenReturn("/user/create?userId=test&password=123&name=John&email=test@example.com");

        //when
        userController.doGet(httpRequestMock, httpResponseMock);

        //then
        verify(userServiceMock, times(1)).signUp(any(UserSignUpDto.class));
        verify(httpResponseMock, times(1)).setStatus(Status.REDIRECT);
        verify(httpResponseMock, times(1)).setLocation("/user/login.html");
    }

    @Test
    @DisplayName("url이 /user/create 들어왔지만 정상적이지 않은 인자값을 갖고 있을때 BadRequestException을 return한다.")
    void ThrowBadRequestExceptionWhenInvalidRequest() {
        //given
        HttpRequest httpRequestMock = mock(HttpRequest.class);
        HttpResponseDto httpResponseMock = mock(HttpResponseDto.class);
        StartLine startLineMock = mock(StartLine.class);

        when(httpRequestMock.getStartLine()).thenReturn(startLineMock);
        when(httpRequestMock.getStartLine().getPathUrl()).thenReturn("/user/create?userId=test");

        //when & then
        assertThrows(BadRequestException.class, () -> userController.doGet(httpRequestMock, httpResponseMock), "Please fill in all the necessary factors");
    }
}
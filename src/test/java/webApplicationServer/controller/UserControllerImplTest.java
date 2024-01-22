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

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class UserControllerImplTest {
    private UserController userController;
    private FakeUserService userServiceMock;
    @BeforeEach
    public void setUP(){
        userServiceMock = new FakeUserService();
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
        String fakePathUrl = "/user/create";
        byte[] fakeBody = "userId=dlwnsgus0&password=rdwg6867&name=%EC%9D%B4%EC%A4%80%ED%98%84&email=dlwnsgus07%40naver.com".getBytes();
        FakeHttpRequest httpRequestMock = new FakeHttpRequest(fakePathUrl, fakeBody);
        FakeHttpResponseDto httpResponseDtoMock = new FakeHttpResponseDto();
        //when
        userController.doGet(httpRequestMock, httpResponseDtoMock);

        //then
        assertThat(userServiceMock.wasMethodCalled("signUp")).isTrue();
        assertThat(httpResponseDtoMock.getStatus()).isEqualTo(Status.REDIRECT);
        assertThat(httpResponseDtoMock.getOptionHeader().containsKey("Location")).isTrue();
        assertThat(httpResponseDtoMock.getOptionHeader().get("Location")).isEqualTo("/index.html");
    }

    @Test
    @DisplayName("url이 /user/create 들어왔지만 정상적이지 않은 인자값을 갖고 있을때 BadRequestException을 return한다.")
    void ThrowBadRequestExceptionWhenInvalidRequest() {
        //given
        String fakePathUrl = "/user/create";
        byte[] fakeBody = "userId=&password=rdwg6867&name=%EC%9D%B4%EC%A4%80%ED%98%84&email=dlwnsgus07%40naver.com".getBytes();

        FakeHttpRequest httpRequestMock = new FakeHttpRequest(fakePathUrl, fakeBody);
        FakeHttpResponseDto httpResponseDtoMock = new FakeHttpResponseDto();

        //when & then
        assertThrows(BadRequestException.class, () -> userController.doGet(httpRequestMock, httpResponseDtoMock), "Please fill in all the necessary factors");
    }
}

package controller;

import dto.request.UserLoginDto;
import dto.request.UserSignUpDto;
import model.HttpRequest;
import model.HttpResponse;
import service.SessionManager;
import service.UserService;

import static model.HttpStatus.BAD_REQUEST;
import static service.SessionManager.createSession;
import static service.SessionManager.existingSession;

public class UserController {
    private static final String COOKIE_NAME = "sid";
    public static HttpResponse createUser(HttpRequest httpRequest){
        try{
            UserSignUpDto userSignUpDto = UserSignUpDto.from(httpRequest.getBody());
            UserService.signUp(userSignUpDto);
            return HttpResponse.redirect("/index.html");
        }catch (IllegalArgumentException | NullPointerException e){//중복 회원인 경우, 회원가입란에 빈란이 있는 경우
            return HttpResponse.errorResponse(BAD_REQUEST, e.getMessage());
        }
    }

    public static HttpResponse login(HttpRequest httpRequest){
        try{
            UserLoginDto userLoginDto = UserLoginDto.from(httpRequest.getBody());
            String userId = UserService.login(userLoginDto.getUserId(), userLoginDto.getPassword());
            HttpResponse httpResponse = HttpResponse.redirect("/index.html");

            if(existingSession(httpRequest, userId))
            {
                httpResponse.addCookie(COOKIE_NAME, httpRequest.getSessionId());
                return httpResponse;
            }

            httpResponse.addCookie(COOKIE_NAME, createSession(userId));
            return httpResponse;
        }catch (IllegalArgumentException | NullPointerException e){
            return HttpResponse.redirect("/user/login_failed.html");
        }
    }
}

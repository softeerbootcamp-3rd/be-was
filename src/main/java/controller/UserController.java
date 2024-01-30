package controller;

import dto.request.UserLoginDto;
import dto.request.UserSignUpDto;
import model.HttpRequest;
import model.HttpResponse;
import service.UserService;

import static model.HttpStatus.BAD_REQUEST;
import static service.SessionManager.createSession;
import static service.SessionManager.existingSession;

public class UserController {
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

            if(httpRequest.getSessionId() != null){
                if(existingSession(userId, httpRequest.getSessionId(), httpResponse)){//이미 세션이 존재하는 경우
                    return httpResponse;
                }
            }

            createSession(userId, httpResponse);//세션이 없는 경우 새로운 세션 생성
            return httpResponse;
        }catch (IllegalArgumentException | NullPointerException e){
            return HttpResponse.redirect("/user/login_failed.html");
        }
    }
}

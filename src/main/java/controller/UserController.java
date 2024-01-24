package controller;

import dto.request.UserLoginDto;
import dto.request.UserSignUpDto;
import model.HttpRequest;
import model.HttpResponse;
import service.UserService;

import static model.HttpStatus.BAD_REQUEST;

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
            String redirectUrl = UserService.login(userLoginDto);
            return HttpResponse.redirect(redirectUrl);
        }catch (NullPointerException e){//로그인란에 빈란이 있는 경우
            return HttpResponse.errorResponse(BAD_REQUEST, e.getMessage());
        }
    }
}

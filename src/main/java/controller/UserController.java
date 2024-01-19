package controller;

import dto.request.UserDto;
import model.HttpRequest;
import model.HttpResponse;
import service.UserService;

import static webserver.HttpStatus.BAD_REQUEST;
import static webserver.HttpStatus.FOUND;

public class UserController {
    public static HttpResponse createUser(HttpRequest httpRequest){
        try{
            UserDto userDto = UserDto.from(httpRequest.getUri().getQuery());
            UserService.signUp(userDto);
            System.out.println(HttpResponse.redirect(FOUND, "/index.html").toString());
            return HttpResponse.redirect(FOUND, "/index.html");
        }catch (IllegalArgumentException e){
            return HttpResponse.errorResponse(BAD_REQUEST, e.getMessage());
        }
    }
}

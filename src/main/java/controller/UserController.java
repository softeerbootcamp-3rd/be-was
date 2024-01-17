package controller;

import dto.request.UserDto;
import model.HttpRequest;
import model.HttpResponse;
import service.UserService;

import static webserver.HttpStatus.BAD_REQUEST;
import static webserver.HttpStatus.FOUND;

public class UserController {
    public static HttpResponse createUser(HttpRequest httpRequest){
        //컨트롤러에서 dto 만들어서 넘겨주기~
        UserDto userDto = UserDto.from(httpRequest.getUri().getQuery());

        try{
            UserService.signUp(userDto);
            return HttpResponse.redirect(FOUND, "/index.html");//예외가
        }catch (IllegalArgumentException e){
            return HttpResponse.response400(BAD_REQUEST, e.getMessage());
        }
    }

}

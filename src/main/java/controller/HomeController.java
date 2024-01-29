package controller;

import model.HttpRequest;
import model.HttpResponse;
import model.User;
import service.SessionManager;

import java.io.IOException;

import static model.HttpStatus.INTERNAL_SERVER_ERROR;

public class HomeController {
    public static HttpResponse home(HttpRequest httpRequest){
        try {
            HttpResponse httpResponse = HttpResponse.response200(httpRequest.getExtension(), httpRequest.getPath());

            if(httpRequest.getSessionId() != null){//로그인 된 경우
                User user = SessionManager.findUserBySessionId(httpRequest.getSessionId());
                httpResponse.setBody("{{login}}", "<li><a>" + user.getName() + "님</a></li>");
            }

            httpResponse.setBody("{{login}}", "<li><a href=\"user/login.html\" role=\"button\">로그인</a></li>");
            return httpResponse;
        } catch (IOException e){
            return HttpResponse.errorResponse(INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}

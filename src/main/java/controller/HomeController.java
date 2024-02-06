package controller;

import model.HttpRequest;
import model.HttpResponse;
import model.User;
import service.HomeService;
import service.SessionManager;

import java.io.IOException;

import static model.HttpStatus.INTERNAL_SERVER_ERROR;

public class HomeController {
    public static HttpResponse home(HttpRequest httpRequest){
        try {
            HttpResponse httpResponse = HttpResponse.response200(httpRequest.getExtension(), httpRequest.getPath());
            String loginBody = "<li><a href=\"user/login.html\" role=\"button\">로그인</a></li>";
            String logoutBody = "<li><a href=\"user/form.html\" role=\"button\">회원가입</a></li>";

            if (httpRequest.getSessionId() != null) {//로그인 된 경우
                User user = SessionManager.findUserBySessionId(httpRequest.getSessionId());
                loginBody = "<li><a>" + user.getName() + "님</a></li>";
                logoutBody = "<li><a href=\"user/logout\" role=\"button\">로그아웃</a></li>";
            }

            httpResponse.setBody("{{login}}", loginBody);
            httpResponse.setBody("{{logout}}", logoutBody);
            return httpResponse;
        } catch (IOException e) {
            return HttpResponse.errorResponse(INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    public static HttpResponse profile(HttpRequest httpRequest){
        try {
            if(httpRequest.getSessionId() == null){//로그인 되지 않은 경우
                return HttpResponse.redirect("/user/login.html");//로그인 창으로 리다이렉트
            }

            HttpResponse httpResponse = HttpResponse.response200(httpRequest.getExtension(), httpRequest.getPath());
            User user = SessionManager.findUserBySessionId(httpRequest.getSessionId());
            String userProfile = HomeService.getUserProfile(user);
            httpResponse.setBody("{{profile}}", userProfile);

            return httpResponse;
        } catch (IOException e){
            return HttpResponse.errorResponse(INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}

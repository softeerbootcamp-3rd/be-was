package controller;

import model.User;
import service.UserService;
import utils.HtmlBuilder;
import utils.ResourceReader;
import webserver.http.request.HttpRequest;
import webserver.http.response.HttpResponse;
import webserver.http.response.HttpResponseBuilder;
import webserver.http.response.enums.HttpStatus;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class UserController {
    private final UserService userService = new UserService();

    public HttpResponse signUp(HttpRequest request) {
        Map<String, String> bodyParams = request.getBody();
        if(userService.signUp(bodyParams)){
            // 회원가입 성공
            return HttpResponseBuilder.getInstance().createRedirectResponse(HttpStatus.FOUND, "/index.html");
        }
        // 회원가입 실패
        return HttpResponseBuilder.getInstance().createErrorResponse(HttpStatus.BAD_REQUEST, "SignUp Failed".getBytes());
    }

    public HttpResponse login(HttpRequest request){
        Map<String, String> bodyParams = request.getBody();
        String sessionId = userService.login(bodyParams);
        if (sessionId != null) {
            // 로그인 성공
            HttpResponse response = HttpResponseBuilder.getInstance().createRedirectResponse(HttpStatus.FOUND, "/index.html");
            HttpResponseBuilder.getInstance().addSessionCookie(response, sessionId);
            return response;
        }
        // 로그인 실패
        return HttpResponseBuilder.getInstance().createRedirectResponse(HttpStatus.FOUND, "/user/login_failed.html");
    }

    public HttpResponse listUsers() throws IOException {
        List<User> loggedInUsers = userService.getLoginUsers();
        String userTableHtml = HtmlBuilder.userListHtml(loggedInUsers);

        String htmlTemplate = ResourceReader.getInstance().getFileTemplate("/user/list.html");
        String finalHtml = htmlTemplate.replace("<!-- UserList -->", userTableHtml);

        byte[] body = finalHtml.getBytes();
        return HttpResponseBuilder.getInstance().createSuccessResponse(HttpStatus.OK, body);
    }

}

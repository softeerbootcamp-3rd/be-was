package controller;

import annotation.Controller;
import annotation.RequestMapping;
import model.User;
import util.RequestParser;
import util.ResourceLoader;
import util.SessionManager;
import webserver.HttpRequest;
import webserver.HttpResponse;

import java.io.IOException;
import java.util.Map;

@Controller
public class ResourceController {

    @RequestMapping(method = "GET", path = "/index.html")
    public static HttpResponse index(HttpRequest request) throws IOException {
        HttpResponse response = ResourceLoader.getFileResponse("/index.html");
        String fileContent = response.getBody();
        String sid = RequestParser.parseCookie(request.getHeader().get("Cookie")).get("SID");
        User user;
        if ((user = SessionManager.getUserBySessionId(sid)) != null) {
            response.setBody(fileContent.replace("{user-name}", user.getName())
                    .replace("{login-btn}", ""));
        } else {
            response.setBody(fileContent.replace("{user-name}", "")
                    .replace("{login-btn}",
                            "<li><a href=\"user/login.html\" role=\"button\">로그인</a></li>"));
        }
        return response;
    }

    @RequestMapping(method = "GET", path = "/user/list")
    public static HttpResponse userList(HttpRequest request) throws IOException {
        String sid = RequestParser.parseCookie(request.getHeader().get("Cookie")).get("SID");
        if (SessionManager.getUserBySessionId(sid) == null)
            return ResourceLoader.getFileResponse("/index.html");

        HttpResponse response = ResourceLoader.getFileResponse("/user/list.html");
        return response;
    }
}

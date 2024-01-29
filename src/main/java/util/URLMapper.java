package util;

import controller.HomeController;
import controller.ResourceController;
import controller.UserController;
import model.HttpRequest;
import model.HttpResponse;

import java.util.HashMap;
import java.util.Map;

import java.util.function.Function;

public class URLMapper {
    public static final Map<String, Function<HttpRequest, HttpResponse>> URL_MAPPING = new HashMap<>();

    static {
        URL_MAPPING.put("POST /user/create", UserController::createUser);
        URL_MAPPING.put("POST /user/login", UserController::login);

        URL_MAPPING.put("GET /user/list.html", UserController::userList);
        URL_MAPPING.put("GET /index.html", HomeController::home);
        URL_MAPPING.put("GET /user/profile.html", HomeController::profile);
        URL_MAPPING.put("GET /user/logout", UserController::logout);
    }

    //찾으면 찾은 컨트롤러 반환, 못 찾으면 ResourceController 반환
    public static Function<HttpRequest, HttpResponse> getController(HttpRequest httpRequest) {
        String findController = httpRequest.getMethod() + " " + httpRequest.getUri().getPath();

        return URL_MAPPING.getOrDefault(findController, ResourceController::serveStaticFile);
    }
}

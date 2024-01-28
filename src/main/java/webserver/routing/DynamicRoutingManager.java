package webserver.routing;

import controller.UserController;
import webserver.http.request.HttpRequest;
import webserver.http.request.enums.HttpMethod;
import webserver.http.response.HttpResponse;

import java.io.IOException;

public class DynamicRoutingManager {
    private static final DynamicRoutingManager instance = new DynamicRoutingManager();

    private DynamicRoutingManager() {}

    public static DynamicRoutingManager getInstance(){
        return instance;
    }

    public HttpResponse handleRequest(HttpRequest httpRequest) throws IOException {
        String path = httpRequest.getPath();
        if (httpRequest.getMethod() == HttpMethod.POST && path.equals("/user/create")) {
            UserController userController = new UserController();
            return userController.signUp(httpRequest);
        } else if (httpRequest.getMethod() == HttpMethod.POST && path.equals("/user/login")) {
            UserController userController = new UserController();
            return userController.login(httpRequest);
        } else if (httpRequest.getMethod() == HttpMethod.GET && path.equals("/user/list.html")){
            UserController userController = new UserController();
            return userController.listUsers(httpRequest);
        } else if (httpRequest.getMethod() == HttpMethod.GET && path.equals("/index.html")) {
            UserController userController = new UserController();
            return userController.home(httpRequest);
        }
        return null;
    }
}

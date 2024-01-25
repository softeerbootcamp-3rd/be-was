package webserver.routing;

import controller.UserController;
import webserver.http.request.HttpRequest;
import webserver.http.request.enums.HttpMethod;
import webserver.http.response.HttpResponse;

public class DynamicRoutingManager {
    private static final DynamicRoutingManager instance = new DynamicRoutingManager();

    private DynamicRoutingManager() {}

    public static DynamicRoutingManager getInstance(){
        return instance;
    }

    public HttpResponse handleRequest(HttpRequest httpRequest) {
        String path = httpRequest.getPath();
        if (httpRequest.getMethod() == HttpMethod.POST && path.equals("/user/create")) {
            UserController userController = new UserController();
            return userController.signUp(httpRequest);
        } else if (httpRequest.getMethod() == HttpMethod.POST && path.equals("/user/login")) {
            UserController userController = new UserController();
            return userController.login(httpRequest);
        }

        return null;
    }
}

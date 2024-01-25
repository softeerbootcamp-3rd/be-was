package webserver.routing;

import controller.UserController;
import webserver.http.request.enums.HttpMethod;
import webserver.http.request.HttpRequest;
import webserver.http.response.HttpResponse;
import webserver.http.response.HttpResponseBuilder;
import webserver.http.response.enums.HttpStatus;

public class DynamicRoutingManager {
    public static final byte[] NOT_FOUND_MESSAGE = "The requested resource was not found on this server.".getBytes();
    public static HttpResponse handleRequest(HttpRequest httpRequest) {
        String path = httpRequest.getPath();
        if (httpRequest.getMethod() == HttpMethod.POST && path.equals("/user/create")) {
            UserController userController = new UserController();
            return userController.signUp(httpRequest);
        } else if (httpRequest.getMethod() == HttpMethod.POST && path.equals("/user/login")) {
            UserController userController = new UserController();
            return userController.login(httpRequest);
        }

        return HttpResponseBuilder.getInstance().createErrorResponse(HttpStatus.NOT_FOUND, NOT_FOUND_MESSAGE);
    }
}

package controller;

import db.Database;
import httpmessage.HttpSession;
import httpmessage.request.HttpRequest;
import httpmessage.response.HttpResponse;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.RequestHandler;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class FirstController {
    private final Map<String, Controller> controllerMap = new HashMap<>();
    public FirstController() {
        initControllerMapping();
    }

    public void service(HttpRequest httpRequest, HttpResponse httpResponse) throws IOException {
        final String requestPath =  httpRequest.getPath();
        Controller controller = controllerMap.get(requestPath);

        if(controller == null){
            controller = new HomeController();
        }

        controller.service(httpRequest,httpResponse);

        if(requestPath.contains("html")) {
            new HtmlController().service(httpRequest,httpResponse);
        }

    }

    private void initControllerMapping() {
        controllerMap.put("/", new HomeController());
        controllerMap.put("/index.html", new HomeController());
        controllerMap.put("/user/create", new UserCreateController());
        controllerMap.put("/user/login", new UserLoginController());
        controllerMap.put("/user/list", new UserListController());
    }

}

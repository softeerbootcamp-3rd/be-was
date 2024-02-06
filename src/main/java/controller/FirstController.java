package controller;

import httpmessage.request.HttpRequest;
import httpmessage.response.HttpResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class FirstController {
    private final Map<String, Controller> controllerMap = new HashMap<>();
    public FirstController() {
        initControllerMapping();
    }

    public void service(HttpRequest httpRequest, HttpResponse httpResponse) throws IOException, ClassNotFoundException {
        final String requestPath =  httpRequest.getPath();

        Controller controller = controllerMap.get(requestPath);

        if(requestPath.startsWith("/qna/detail?id=")) controller = controllerMap.get("/qna/detail");

        else if(controller == null){
            controller = controllerMap.get("/");
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
        controllerMap.put("/qna/write", new WritingController());
        controllerMap.put("/qna/detail", new WritingDetailController());
    }

}

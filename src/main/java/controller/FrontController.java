package controller;

import http.HttpRequest;
import http.HttpResponse;

import java.util.HashMap;
import java.util.Map;

public class FrontController {
    private Map<String, Controller> controllerMap = new HashMap<>(); //controller list
    private final String RESOURCES_TEMPLATES_URL = "src/main/resources/templates";
    private final String RESOURCES_STATIC_URL = "src/main/resources/static";

    public FrontController() {
        controllerMap.put("/user/create", new UserController());
    }

    public String process(HttpRequest request, HttpResponse response) {
        String url = request.getUrl();
        String method = request.getMethod();

        String path;
        Controller controller = controllerMap.get(url);
        if (controller == null) { //.html
            if (url.endsWith(".html")) {
                path = RESOURCES_TEMPLATES_URL + url;

            } else { //.js .css ...
                path = RESOURCES_STATIC_URL + url;
            }
        } else {
            if (method.equals("GET")) {
                path = controller.process(request, response) + ".html";
            } else if (method.equals("POST")) {
                path = controller.process(request, response) + ".html";
            } else {
                path = null;
            }
        }
        response.setPath(path);
        return path;
    }
}

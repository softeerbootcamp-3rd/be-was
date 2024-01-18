package controller;


import request.HttpRequest;
import util.RequestUrl;

public class HomeController {
    private volatile static HomeController instance;

    private HomeController() {
    }

    public static HomeController getInstance() {
        if (instance == null) {
            instance = new HomeController();
        }
        return instance;
    }

    public String route(HttpRequest httpRequest) {
        String path = httpRequest.getPath();

        if (path.equals(RequestUrl.HOME.getUrl())) {
            return "302 /index.html";
        }
        else {
            return "200 " + path;
        }
    }
}

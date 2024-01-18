package controller;


import request.HttpRequest;
import util.RequestUrl;

import java.io.IOException;
import java.io.OutputStream;

import static util.ResponseBuilder.response;

public class HomeController implements Controller {
    private volatile static HomeController instance;

    private HomeController() {
    }

    public static HomeController getInstance() {
        if (instance == null) {
            instance = new HomeController();
        }
        return instance;
    }

    @Override
    public void route(HttpRequest httpRequest, OutputStream out) throws IOException {
        String path = httpRequest.getPath();

        if (path.equals(RequestUrl.HOME.getUrl())) {
            response("302 /index.html", out);
        }
        else {
            response("200 " + path, out);
        }
    }
}

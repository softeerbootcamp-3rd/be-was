package controller;

import request.HttpRequest;
import util.StatusCode;

import static util.Uri.*;
import static util.StatusCode.*;

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
    public StatusCode handleUserRequest(HttpRequest httpRequest) {
        String URI = httpRequest.getUri();

        if (URI.equals(HOME.getUrl())) {
            return FOUND;
        }
        else {
            return OK;
        }
    }
}

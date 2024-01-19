package controller;

import util.StatusCode;

import static util.RequestUrl.*;
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
    public StatusCode route(String requestLine) {
        String URI = requestLine.split(" ")[1];

        if (URI.equals(HOME.getUrl())) {
            return FOUND;
        }
        else {
            return OK;
        }
    }
}

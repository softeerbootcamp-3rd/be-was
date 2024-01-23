package controller;

import request.HttpRequest;
import util.StatusCode;

import java.io.File;

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
        String uri = httpRequest.getUri();
        String filePath = httpRequest.getFilePath(uri);
        String method = httpRequest.getMethod();

        File file = new File(filePath);

        if (file.exists() && method.equals("GET")) {
            if (uri.equals(HOME.getUri())) return FOUND;
            return OK;
        }

        return NOT_FOUND;
    }
}

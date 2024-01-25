package controller;

import request.http.HttpRequest;
import response.http.HttpResponse;

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
    public HttpResponse handleUserRequest(HttpRequest httpRequest) throws Exception {
        String uri = httpRequest.getUri();
        String filePath = httpRequest.getFilePath(uri);
        String method = httpRequest.getMethod();

        File file = new File(filePath);

        if (file.exists() && method.equals("GET")) {
            if (uri.equals(HOME.getUri())) {
                return new HttpResponse(FOUND, "text/html", HOME_INDEX.getUri(), null);
            }
            return new HttpResponse(OK, filePath);
        }

        return new HttpResponse(NOT_FOUND);
    }
}

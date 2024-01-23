package controller;

import model.User;
import request.HttpRequest;
import response.HttpResponse;
import service.UserService;
import util.Parser;

import java.io.File;
import java.io.IOException;

import static util.Uri.*;
import static util.StatusCode.*;

public class UserController implements Controller {

    private volatile static UserController instance = new UserController();

    private UserController() {
    }

    public static UserController getInstance() {
        if (instance == null) {
            instance = new UserController();
        }
        return instance;
    }

    private final UserService userService = UserService.getInstance();

    @Override
    public HttpResponse handleUserRequest(HttpRequest httpRequest) throws IOException {

        String uri = httpRequest.getUri();
        String filePath = httpRequest.getFilePath(uri);
        String method = httpRequest.getMethod();

        File file = new File(filePath);

        if (file.exists() && method.equals("GET")) {
            return new HttpResponse(OK, filePath);
        }

        if (method.equals("POST")) {
            if (uri.equals(USER_CREATE.getUri())) {
                try {
                    User user = Parser.jsonParser(User.class, httpRequest.getRequestBody());
                    signUp(user);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                return new HttpResponse(FOUND, "text/html", "/index.html");

            }
        }

        return new HttpResponse(NOT_FOUND);
    }

    // 회원가입 요청 처리
    public void signUp(User user) {
        userService.join(user);
    }
}

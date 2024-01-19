package controller;

import model.Response;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;
import service.UserService;
import utils.ParamBuilder;

public class UserController implements Controller {

    private static final UserService userService = new UserService();

    public Response route(String url) {
        if (url.startsWith("/user/create")) {
            return createUser(url);
        } else {
            return getPage(url);
        }
    }

    private Response getPage(String url) {
        String filePath = "src/main/resources/templates" + url;

        try {
            byte[] body = Files.readAllBytes(new File(filePath).toPath());
            return new Response(url, 200, body);
        } catch (IOException e) {
            byte[] body = "404 Not Found".getBytes();
            return new Response(url, 404, body);
        }
    }

    private Response createUser(String url) {
        Map<String, String> params = ParamBuilder.getParams(url);

        try {
            userService.saveUser(params);
        } catch (NullPointerException e) {
            return new Response("", 400, "cannot find parameter.".getBytes());
        } catch (IllegalArgumentException e) {
            return new Response("", 400, "user id already exists.".getBytes());
        }

        String location = "/";
        return new Response(location, 302, "".getBytes());
    }
}

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
        }
        return getPage(url);
    }

    private Response getPage(String url) {
        String filePath = "src/main/resources/templates" + url;

        try {
            byte[] body = Files.readAllBytes(new File(filePath).toPath());
            return new Response(200, url, body);
        } catch (IOException e) {
            byte[] body = "404 Not Found".getBytes();
            return new Response(404, body);
        }
    }

    private Response createUser(String url) {
        Map<String, String> params = ParamBuilder.getParams(url);

        try {
            userService.saveUser(params);
        } catch (NullPointerException e) {
            byte[] body = "cannot find parameter.".getBytes();
            return new Response(400, body);
        } catch (IllegalArgumentException e) {
            byte[] body = "user id already exists.".getBytes();
            return new Response(400, body);
        }

        String location = "/";
        return new Response(302, location);
    }
}

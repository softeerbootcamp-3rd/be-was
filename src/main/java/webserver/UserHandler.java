package webserver;

import controller.UserController;
import dto.UserCreateRequestDto;
import model.HttpStatus;
import model.Request;
import model.Response;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import static util.UrlParser.parseQueryString;
import static webserver.RequestHandler.serveStaticResource;

public class UserHandler {

    private static final String HOME = "/index.html";
    private static final String CREATE = "/create";
    private final UserController userController = new UserController();

    public Response handleUserPath(String url, Request request, DataOutputStream dos) throws IOException {
        if (url.startsWith(CREATE)) {
            return handleUserCreation(url.substring(CREATE.length()), dos);
        }
        return new Response(HttpStatus.OK, serveStaticResource(request));
    }

    public Response handleUserCreation(String query, DataOutputStream dos) {
        try {
            if (!query.startsWith("?")) {
                return new Response(HttpStatus.BAD_REQUEST);
            }
            String decodedQuery = URLDecoder.decode(query.substring(1), StandardCharsets.UTF_8);
            Map<String, String> queryParams = parseQueryString(decodedQuery);
            UserCreateRequestDto userCreateRequestDto = new UserCreateRequestDto(queryParams.get("userId"),
                    queryParams.get("password"), queryParams.get("name"), queryParams.get("email"));
            userController.create(userCreateRequestDto);
            return new Response(HttpStatus.REDIRECT, HOME);
        } catch (IllegalArgumentException e) {
            return new Response(HttpStatus.REDIRECT, "/user/form_failed.html");
        } catch (Exception e) {
            return new Response(HttpStatus.SERVER_ERROR);
        }
    }
}

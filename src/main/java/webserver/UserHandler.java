package webserver;

import controller.UserController;
import dto.UserCreateRequestDto;
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

    private static final String CREATE = "/create";
    private static final UserController userController = new UserController();

    public static Response handleUserPath(String url, Request request, DataOutputStream dos) throws IOException {
        if (url.startsWith(CREATE)) {
            return handleUserCreation(url.substring(CREATE.length()), dos);
        }
        return new Response(200, serveStaticResource(request));
    }

    public static Response handleUserCreation(String query, DataOutputStream dos) {
        try {
            if (!query.startsWith("?")) {
                return new Response(400, "잘못된 쿼리 문자열".getBytes());
            }

            String decodedQuery = URLDecoder.decode(query.substring(1), StandardCharsets.UTF_8);
            Map<String, String> queryParams = parseQueryString(decodedQuery);
            UserCreateRequestDto userCreateRequestDto = new UserCreateRequestDto(queryParams.get("userId"),
                    queryParams.get("password"),
                    queryParams.get("name"),
                    queryParams.get("email"));
            userController.create(userCreateRequestDto);
            return new Response(302, new byte[0], "/index.html");

        } catch (Exception e) {
            return new Response(500, "서버 내부 오류".getBytes());
        }
    }
}

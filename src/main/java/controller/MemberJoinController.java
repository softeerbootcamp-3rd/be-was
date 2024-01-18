package controller;

import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import request.HttpRequest;
import response.HttpResponse;
import webserver.RequestHandler;

import java.util.HashMap;
import java.util.Map;

public class MemberJoinController implements Controller {

    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);
    @Override
    public void process(HttpRequest request, HttpResponse response) {
        String fullUrl = request.getUrl();
        String queryString = fullUrl.substring(fullUrl.indexOf("?") + 1);
        Map<String, String> params = parse(queryString);
        User user = createUser(params);
        logger.debug("User : {}", user);

        Map<String, String> headers = new HashMap<>();
        headers.put("Location", "/index.html");
        response.setResponse("302", "FOUND", null, headers);
    }

    public static Map<String, String> parse(String query) {
        Map<String, String> params = new HashMap<>();

        String[] pairs = query.split("&");
        for (String pair : pairs) {
            String[] keyValue = pair.split("=");
            params.put(keyValue[0], keyValue[1]);
        }

        return params;
    }

    public static User createUser(Map<String, String> params) {
        String userId = params.get("userId");
        String password = params.get("password");
        String name = params.get("name");
        String email = params.get("email");

        User user = new User(userId, password, name, email);
        return user;
    }

}

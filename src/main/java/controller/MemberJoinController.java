package controller;

import model.User;
import request.HttpRequest;
import response.HttpResponse;

import java.util.HashMap;
import java.util.Map;

public class MemberJoinController implements Controller {
    @Override
    public void process(HttpRequest request, HttpResponse response) {
        String fullUrl = request.getUrl();
        String queryString = fullUrl.substring(fullUrl.indexOf("?") + 1);
        Map<String, String> params = parse(queryString);
        User user = createUser(params);
        System.out.println(user);
        set302Response(response, null);
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

    private static void set302Response(HttpResponse response, byte[] body) {
        response.setVersion("HTTP/1.1");
        response.setStatusCode("302");
        response.setStatusMessage("FOUND");
        response.setBody(body);

        response.getHeaders().put("Location", "/index.html");
    }
}

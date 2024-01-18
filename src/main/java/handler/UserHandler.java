package handler;

import db.Database;
import http.HttpRequest;
import http.HttpResponse;
import http.header.ResponseHeader;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import http.status.HttpStatus;

import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;

public class UserHandler {
    private static final Logger logger = LoggerFactory.getLogger(UserHandler.class);

    public static UserHandler getInstance() {
        return LazyHolder.INSTANCE;
    }

    private static class LazyHolder {
        private static final UserHandler INSTANCE = new UserHandler();
    }

    public void handle(HttpRequest request, HttpResponse response) {
        Map<String, String> queryString = request.getQueryString();
        String userId = queryString.getOrDefault("userId", null);
        String password = queryString.getOrDefault("password", null);
        String name = queryString.getOrDefault("name", null);
        String email = queryString.getOrDefault("email", null);

        if (Arrays.asList(userId, password, name, email).contains(null)) {
            throw new RuntimeException("user property cannot be null");
        }

        Database.addUser(new User(userId, password, name, email));

        Map<String, String> properties = new TreeMap<>();
        properties.put("Location", "/user/login.html");

        response.setHeader(
                ResponseHeader.of(HttpStatus.SEE_OTHER, properties)
        );
    }
}

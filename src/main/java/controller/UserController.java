package controller;

import db.Database;
import model.User;
import service.UserService;
import util.RequestUrl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;

public class UserController {
    private final RequestUrl url;
    private final UserService userService;

    public UserController(RequestUrl url) {
        this.url = url;
        this.userService = new UserService();
    }

    public byte[] run() throws IOException {
        String path = url.getPath();

        String[] tokens = path.split("/");
        String lastPath = tokens[tokens.length - 1];

        if (lastPath.equals("create")) {
            String userId = create();
            return (userId + " created").getBytes();
        } else
            return Files.readAllBytes(new File("./src/main/resources/templates" + path).toPath());
    }

    private String create() {
        HashMap<String, String> query = url.getQuery();

        if (query == null)
            throw new RuntimeException();

        String userId = query.get("userId");
        String password = query.get("password");
        String name = query.get("name");
        String email = query.get("email");

        return userService.create(userId, password, name, email);
    }
}
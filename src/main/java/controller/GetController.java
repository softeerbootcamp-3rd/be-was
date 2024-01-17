package controller;

import db.Database;
import model.User;
import util.ResourceLoader;
import util.ResponseBuilder;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;

public class GetController {
    
    public static void getMethod(OutputStream out, String requestPath) {
        if (requestPath.equals("/")) {
            getMain(out);
        }
        else if (requestPath.equals("/index.html")) {
            getIndexHtml(out, requestPath);
        }
        else if (requestPath.startsWith("/user/create")) {
            signup(out, requestPath);
        }
        else {
            getStaticFile(out, requestPath);
        }
    }

    private static void getMain(OutputStream out) {
        byte[] body = "Hello World!".getBytes();
        DataOutputStream dos = new DataOutputStream(out);

        ResponseBuilder.response200Header(dos, body.length, "text/html;charset=utf-8");
        ResponseBuilder.responseBody(dos, body);
    }

    private static void getIndexHtml(OutputStream out, String requestPath) {
        String filePath = "src/main/resources/templates";
        String contentType = "text/html;charset=utf-8";
        DataOutputStream dos = new DataOutputStream(out);

        try {
            byte[] body = Files.readAllBytes(new File(filePath + requestPath).toPath());

            ResponseBuilder.response200Header(dos, body.length, contentType);
            ResponseBuilder.responseBody(dos, body);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void getStaticFile(OutputStream out, String requestPath) {
        String contentType = "text/html;charset=utf-8";
        String filePath = "src/main/resources/templates";
        DataOutputStream dos = new DataOutputStream(out);

        if (!requestPath.endsWith(".html")) {
            contentType = ResourceLoader.getContentType(requestPath);
            filePath = "src/main/resources/static";
        }

        try {
            byte[] body = Files.readAllBytes(new File(filePath + requestPath).toPath());
            ResponseBuilder.response200Header(dos, body.length, contentType);
            ResponseBuilder.responseBody(dos, body);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void signup(OutputStream out, String requestPath) {
        String[] userArray = requestPath.split("\\?")[1].split("&");
        String userId = userArray[0];
        String password = userArray[1];
        String name = userArray[2];
        String email = userArray[3];

        User user = new User(userId, password, name, email);
        Database.addUser(user);
        ResponseBuilder.redirect(out, "/index.html");
    }

}


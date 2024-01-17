package controller;

import db.Database;
import model.Request;
import model.Response;
import model.User;
import service.UserService;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class MainController {

    public static Response control(Request request) throws IOException {
        byte[] body = Files.readAllBytes(new File("./src/main/resources/templates/404error.html").toPath());
        String statusCode = "404";
        String method = request.getMethod();
        String url = request.getUrl();
        String mimeType = request.getMimeType();

        if(method.equals("GET")) {
            if (url.equals("/")) {
                body = Files.readAllBytes(new File("./src/main/resources/templates/index.html").toPath());
                statusCode = "302";
            } else if (url.equals("/user/create")) {
                User user = UserService.create(request.getParam());
                if (user != null) {
                    Database.addUser(user);
                    statusCode = "302";
                    body = Files.readAllBytes(new File("./src/main/resources/templates/index.html").toPath());
                }
                else {
                    statusCode = "200";
                    body = Files.readAllBytes(new File("./src/main/resources/templates/user/form.html").toPath());
                }
            } else {
                if (mimeType.equals("text/html"))
                    url = "./src/main/resources/templates" + url;
                else
                    url = "./src/main/resources/static" + url;
                File searchedFile = new File(url);
                if(searchedFile.exists()) {
                    body = Files.readAllBytes(new File(url).toPath());
                    statusCode = "200";
                }
            }
        }
        else if(method.equals("POST")) {
            // 추후 작성
        }
        else if(method.equals("PATCH")) {
            // 추후 작성
        }
        else if(method.equals("PUT")) {
            // 추후 작성
        }
        else if(method.equals("DELETE")) {
            // 추후 작성
        }
        return new Response(statusCode, body, mimeType);
    }
}

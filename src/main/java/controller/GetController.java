package controller;

import model.Request;
import model.Response;
import service.UserService;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class GetController {

    public static Response getControl(Request request) throws IOException {
        String statusCode = null;
        byte[] body = null;
        String mimeType = request.getMimeType();
        String redirectUrl = null;
        String sessionId = null;

        if (request.getPath().equals("/")) {
            statusCode = "302";
            redirectUrl = "/index.html";
        } else if(request.getPath().startsWith("/user/list")) {
            if(UserService.checkUserLogin(request)) {
                statusCode = "200";
                body = Files.readAllBytes(new File("./src/main/resources/templates/user/list.html").toPath());
            }
            else {
                statusCode = "200";
                body = Files.readAllBytes(new File("./src/main/resources/templates/user/login.html").toPath());
            }
        } else {
            String path = request.getPath();
            if (request.getFile() != null && request.getFile().getType().equals("html"))
                path = "./src/main/resources/templates" + path;
            else
                path = "./src/main/resources/static" + path;
            File searchedFile = new File(path);
            if(searchedFile.exists()) {
                statusCode = "200";
                body = Files.readAllBytes(new File(path).toPath());
            }
            else {
                statusCode = "404";
                body = Files.readAllBytes(new File("./src/main/resources/templates/error404.html").toPath());
            }
        }
        return new Response(statusCode, body, mimeType, redirectUrl, sessionId);
    }
}

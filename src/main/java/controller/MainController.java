package controller;

import db.Database;
import model.Request;
import model.Response;
import model.User;
import model.UserInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.UserService;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class MainController {
    private static final Logger logger = LoggerFactory.getLogger(MainController.class);

    public static Response control(Request request) throws IOException {
        String statusCode = null;
        byte[] body = null;
        String mimeType = request.getMimeType();
        String redirectUrl = null;
        String base = "./src/main/resources";
        String path = request.getPath();
        String method = request.getMethod();

        if(method.equals("GET")) {
            if (path.equals("/")) {
                statusCode = "302";
                redirectUrl = "/index.html";
            } else if (path.equals("/user/create")) {
                User user = UserService.create(new UserInfo(request.getParam()));
                if (user != null) {
                    statusCode = "302";
                    redirectUrl = "/index.html";
                    logger.debug("회원가입 성공!  " + user.toString());
                }
                else {
                    statusCode = "200";
                    body = Files.readAllBytes(new File("./src/main/resources/templates/user/form.html").toPath());
                    logger.debug("회원가입 실패!");
                }
            } else {
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
                    body = Files.readAllBytes(new File(base + "/templates/error404.html").toPath());
                }
            }
        }
        else if(method.equals("POST")) {
            //TODO
        }
        else if(method.equals("PATCH")) {
            //TODO
        }
        else if(method.equals("PUT")) {
            //TODO
        }
        else if(method.equals("DELETE")) {
            //TODO
        }

        return new Response(statusCode, body, mimeType, redirectUrl);
    }
}

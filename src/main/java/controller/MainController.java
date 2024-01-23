package controller;

import db.Database;
import model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.UserService;

import javax.xml.crypto.Data;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.UUID;

public class MainController {
    private static final Logger logger = LoggerFactory.getLogger(MainController.class);

    public static Response control(Request request) throws IOException {
        String statusCode = null;
        byte[] body = null;
        String mimeType = request.getMimeType();
        String redirectUrl = null;
        String sessionId = null;
        String base = "./src/main/resources";
        String path = request.getPath();
        String method = request.getMethod();
        String clientSessionId = request.getCookie().get("sessionId");

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
            } else if(path.equals("/user/list")) {
                if(clientSessionId != null && Session.containsSessionId(clientSessionId)) {
                    statusCode = "200";
                    body = Files.readAllBytes(new File("./src/main/resources/templates/user/list.html").toPath());
                }
                else {
                    statusCode = "200";
                    body = Files.readAllBytes(new File("./src/main/resources/templates/user/login.html").toPath());
                }
            }
            else {
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
            if(path.equals("/user/create")) {
                User user = UserService.create(new UserInfo(request.getBody()));
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
            }
            else if(path.equals("/user/login")) {
                String target = request.getBody().getOrDefault("userId", "");
                User user = Database.findUserById(target);
                if(user == null) {
                    statusCode = "200";
                    body = Files.readAllBytes(new File("./src/main/resources/templates/user/login_failed.html").toPath());
                    logger.debug("로그인 실패!");
                }
                else {
                    if(user.getPassword().equals(request.getBody().getOrDefault("password", ""))) {
                        statusCode = "302";
                        redirectUrl = "/index.html";
                        sessionId = String.valueOf(UUID.randomUUID());
                        Session.addSession(sessionId, user);
                        logger.debug("로그인!  " + user.toString());
                    }
                    else {
                        statusCode = "200";
                        body = Files.readAllBytes(new File("./src/main/resources/templates/user/login_failed.html").toPath());
                        logger.debug("로그인 실패!");
                    }
                }
            }
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

        return new Response(statusCode, body, mimeType, redirectUrl, sessionId);
    }
}
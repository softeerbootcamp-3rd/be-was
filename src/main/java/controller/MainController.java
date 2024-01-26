package controller;

import db.Database;
import model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.UserService;
import util.Util;

import java.io.*;
import java.io.File;
import java.util.Collection;
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

        if(request.getMethod().equals("GET")) {
            if (path.equals("/")) {
                statusCode = "302";
                redirectUrl = "/index.html";
            } else if(path.equals("/user/list")) {
                if(UserService.checkUserLogin(request)) {
                    statusCode = "302";
                    redirectUrl = "/user/list.html";
                }
                else {
                    statusCode = "302";
                    redirectUrl = "/user/login.html";
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
                    if(request.getFile().getType().equals("html")) {
                        StringBuilder stringBuilder = new StringBuilder();
                        if(UserService.checkUserLogin(request)) {
                            Util.readFile(stringBuilder, "./src/main/resources/templates/new/navbar_login.html");
                        }
                        else
                            Util.readFile(stringBuilder, "./src/main/resources/templates/new/navbar_logout.html");
                        if(request.getFile().getName().equals("list.html")) {
                            Collection<User> userList = Database.findAll();
                            Util.readFile(stringBuilder, "./src/main/resources/templates/new/list1.html");
                            int i=0;
                            for(User user : userList) {
                                stringBuilder.append("<tr><th scope=\"row\">" + ++i + "</th> <td>" + user.getUserId() + "</td> <td>"
                                        + user.getName() + "</td> <td>" + user.getEmail() + "</td><td><a href=\"#\" class=\"btn btn-success\" role=\"button\">수정</a></td></tr>");
                            }
                            Util.readFile(stringBuilder, "./src/main/resources/templates/new/list2.html");
                        }
                        else {
                            Util.readFile(stringBuilder, path);
                        }
                        String string = stringBuilder.toString();
                        if(UserService.checkUserLogin(request))
                            string = string.replaceAll("#이름", Session.findBySessionId(request.getCookie().get("sessionId")).getName()    );
                        body = string.getBytes();
                    }
                    else {
                        File file = new File(path);
                        byte[] data = new byte[(int) file.length()];

                        try (FileInputStream fileInputStream = new FileInputStream(file)) {
                            int bytesRead = fileInputStream.read(data);
                            if (bytesRead != file.length()) {
                                throw new IOException("Could not read the entire file");
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        body = data;
                    }
                }
                else {
                    statusCode = "302";
                    redirectUrl = "/error404.html";
                }
            }
        }
        else if(method.equals("POST")) {
            if(path.equals("/user/create")) {
                User user = UserService.create(request.getBody());
                if (user != null) {
                    statusCode = "302";
                    redirectUrl = "/index.html";
                    logger.debug("회원가입 성공!  " + user.toString());
                }
                else {
                    statusCode = "302";
                    redirectUrl = "/user/form.html";
                    logger.debug("회원가입 실패!");
                }
            }
            else if(path.equals("/user/login")) {
                String target = request.getBody().getOrDefault("userId", "");
                User user = Database.findUserById(target);
                if(user == null) {
                    statusCode = "302";
                    redirectUrl = "/user/login_failed.html";
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
                        statusCode = "302";
                        redirectUrl = "/user/login_failed.html";
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
package controller;

import db.Database;
import model.Request;
import model.Response;
import model.Session;
import model.User;
import service.UserService;
import util.Util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Collection;

public class GetController {

    public static Response controlGet(Request request) {
        String statusCode = null;
        byte[] body = null;
        String mimeType = request.getMimeType();
        String redirectUrl = null;
        String sessionId = null;
        String base = "./src/main/resources";
        String path = request.getPath();
        String method = request.getMethod();
        Response response = new Response(statusCode, body, mimeType, redirectUrl, sessionId);

        if (path.equals("/")) {
            response.setStatusCode("302");
            response.setRedirectUrl("/index.html");
            return response;
        }
        if(path.equals("/user/list")) {
            if(UserService.checkUserLogin(request)) {
                response.setStatusCode("302");
                response.setRedirectUrl("/user/list.html");
            }
            else {
                response.setStatusCode("302");
                response.setRedirectUrl("/user/login.html");
            }
            return response;
        }
        else {
            if (request.getFile() != null && request.getFile().getType().equals("html"))
                path = "./src/main/resources/templates" + path;
            else
                path = "./src/main/resources/static" + path;
            File searchedFile = new File(path);
            if(searchedFile.exists()) {
                response.setStatusCode("200");
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
                    response.setBody(string.getBytes());
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
                    response.setBody(data);
                }
            }
            else {
                response.setStatusCode("302");
                response.setRedirectUrl("/error404.html");
            }
        }
        return response;
    }
}

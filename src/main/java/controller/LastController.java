package controller;

import db.Database;
import db.SessionStorage;
import model.Request;
import model.Response;
import model.User;
import utils.Util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;

public class LastController {

    public static void route(Request request, Response response, boolean login) {
        String path = request.getPath();
        String filePath;

        if(request.getMimeType().equals("text/html"))
            filePath = "./src/main/resources/templates" + path;
        else
            filePath = "./src/main/resources/static" + path;
        File file = new File(filePath);

        if(!file.exists()) {
            response.setStatusCode("302");
            response.addHeader("Location", "/error404.html");
            return;
        }

        if(request.getMimeType().equals("text/html")) {
            HashMap<String, String> replace = new HashMap<>();
            StringBuilder stringBuilder = new StringBuilder();

            if(login) {
                replace.put("{{login}}",
                        "<li>" + SessionStorage.findBySessionId(request.getSessionId()).getUserId() + "</li>" +
                        "<li><a href=\"/user/logout\" role=\"button\">로그아웃</a></li>\n" +
                        "<li><a href=\"/user/modify\" role=\"button\">개인정보수정</a></li>");
            }
            else {
                replace.put("{{login}}",
                        "<li><a href=\"/user/login.html\" role=\"button\">로그인</a></li>\n" +
                        "<li><a href=\"/user/form.html\" role=\"button\">회원가입</a></li>");
            }
            if(path.equals("/user/list.html")) {
                Collection<User> collection = Database.findAll();
                StringBuilder sb = new StringBuilder();
                int idx = 0;
                for(User user : collection) {
                    sb.append("<tr><th scope=\"row\">");
                    sb.append(String.valueOf(++idx));
                    sb.append("</th> <td>");
                    sb.append(user.getUserId());
                    sb.append("</td> <td>");
                    sb.append(user.getName());
                    sb.append("</td> <td>");
                    sb.append(user.getEmail());
                    sb.append("</td><td><a href=\"#\" class=\"btn btn-success\" role=\"button\">수정</a></td></tr>");
                }
                replace.put("{{list}}", sb.toString());
            }

            String html = Util.readFile(stringBuilder, filePath, replace);
            byte[] data = html.getBytes();

            response.setStatusCode("200");
            response.setBody(data);
            response.addHeader("Content-Type", "text/html;charset=utf-8");
            response.addHeader("Content-Length", String.valueOf(data.length));
        }
        else {
            byte[] data = new byte[(int) file.length()];

            try (FileInputStream fileInputStream = new FileInputStream(file)) {
                int bytesRead = fileInputStream.read(data);
                if (bytesRead != file.length()) {
                    throw new IOException("Could not read the entire file");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            response.setStatusCode("200");
            response.setBody(data);
            response.addHeader("Content-Type", request.getMimeType() + ";charset=utf-8");
            response.addHeader("Content-Length", String.valueOf(data.length));
        }
    }
}

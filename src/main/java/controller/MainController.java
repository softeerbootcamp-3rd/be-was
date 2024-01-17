package controller;

import db.Database;
import model.Request;
import model.User;
import service.UserService;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;

public class MainController {

    public static HashMap<String, byte[]> control(Request request) throws IOException {
        byte[] body = Files.readAllBytes(new File("./src/main/resources/templates/404error.html").toPath());
        byte[] statusCode = ("404").getBytes();
        HashMap<String, byte[]> returnData = new HashMap<>();

        String method = request.getMethod();
        String url = request.getUrl();
        String mimeType = request.getMimeType();

        if(method.equals("GET")) {
            if (url.equals("/")) {
                body = Files.readAllBytes(new File("./src/main/resources/templates/index.html").toPath());
                statusCode = ("302").getBytes();
            } else if (url.equals("/user/create")) {
                HashMap<String, String> params = request.getParam();
                User user = UserService.create(params);
                if (user != null) {
                    Database.addUser(user);
                    statusCode = ("302").getBytes();
                    body = Files.readAllBytes(new File("./src/main/resources/templates/index.html").toPath());
                    System.out.println("회원가입 완료!! : " + user.toString());
                }
                else {
                    statusCode = ("200").getBytes();
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
                    statusCode = ("200").getBytes();
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
        returnData.put("body", body);
        returnData.put("statusCode", statusCode);
        returnData.put("mimeType", request.getMimeType().getBytes());
        return returnData;
    }
}

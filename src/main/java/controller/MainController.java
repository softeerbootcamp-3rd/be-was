package controller;

import db.Database;
import model.Request;
import model.User;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;

public class MainController {

    public static HashMap<String, byte[]> control(Request request) throws IOException {
        byte[] body = ("알 수 없는 요청입니다!!!").getBytes();
        byte[] statusCode = ("200").getBytes();
        HashMap<String, byte[]> returnData = new HashMap<>();

        String method = request.getMethod();
        String url = request.getUrl();
        String mimeType = request.getMimeType();

        if(method.equals("GET")) {
            if(url.equals("/")) {
                body = Files.readAllBytes(new File("./src/main/resources/templates/index.html").toPath());
                statusCode = ("302").getBytes();
            }
            else if(url.equals("/user/create")) {
                System.out.println("요청이 오긴 하냐????????????/");
                HashMap<String, String> params = request.getParam();
                String userId = params.get("userId");
                String password = params.get("password");
                String name = params.get("name");
                String email = params.get("email");
                User user = new User(userId, password, name, email);
                String result = User.verifyUser(user);
                System.out.println(user.toString());
                if(result.equals("성공")) {
                    Database.addUser(user);
                    statusCode = ("302").getBytes();
                    body = Files.readAllBytes(new File("./src/main/resources/templates/index.html").toPath());
                }
            }
            else {
                if(mimeType.equals("text/html"))
                    url = "./src/main/resources/templates" + url;
                else
                    url = "./src/main/resources/static" + url;
                body = Files.readAllBytes(new File(url).toPath());
            }
        }
        returnData.put("body", body);
        returnData.put("statusCode", statusCode);
        returnData.put("mimeType", request.getMimeType().getBytes());
        return returnData;
    }
}

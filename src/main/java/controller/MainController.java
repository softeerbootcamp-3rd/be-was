package controller;

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

        if(request.getFile() != null) {
            String path = base;
            if (mimeType.equals("text/html")) path += "/templates";
            else path += "/static";
            path += request.getPath();
            File file = new File(path);
            if(file.exists()) {
                body = Files.readAllBytes(new File(path).toPath());
                statusCode = "200";
            }
            else {
                statusCode = "302";
                redirectUrl = "/error404.html";
            }
        }
        else if(request.getPath().equals("/")) { // 홈 리다이렉트
            statusCode = "302";
            redirectUrl = "/index.html";
        }
        else if(request.getPath().equals("/user/create")) {
            User user = UserService.create(new UserInfo(request.getParam()));
            if (user != null) { // 회원가입 성공
                statusCode = "302";
                redirectUrl = "/index.html";
                logger.debug("회원가입 완료!! = " + user.toString());
            }
            else { // 회원가입 실패
                statusCode = "200";
                body = Files.readAllBytes(new File(base + "/templates/user/form.html").toPath());
            }
        }
        else { // 알 수 없는 요청시 404ERROR
            statusCode = "404";
            body = Files.readAllBytes(new File(base + "/templates/error404.html").toPath());
        }

        return new Response(statusCode, body, mimeType, redirectUrl);
    }
}

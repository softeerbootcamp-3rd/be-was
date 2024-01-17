package controller;

import db.Database;
import dto.RequestDto;
import dto.ResponseDto;
import model.User;
import util.HttpStatus;
import util.ResourceLoader;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class GetController {

    public static ResponseDto getMethod(RequestDto requestDto) {
        String requestPath = requestDto.getPath();

        if (requestPath.equals("/")) {
            return getIndexHtml("/index.html");
        } else if (requestPath.equals("/index.html")) {
            return getIndexHtml(requestPath);
        }
        else if (requestPath.startsWith("/user/create")) {
            return signup(requestPath);
        }
        else {
            return getStaticFile(requestPath);
        }
    }

    private static ResponseDto getMain() {
        byte[] body = "Hello World!".getBytes();

        return new ResponseDto(HttpStatus.OK, "text/html;charset=utf-8", body);
    }

    private static ResponseDto getIndexHtml(String requestPath) {

        try {
            String filePath = "src/main/resources/templates";
            String contentType = "text/html;charset=utf-8";
            byte[] body = Files.readAllBytes(new File(filePath + requestPath).toPath());

            return new ResponseDto(HttpStatus.OK, contentType, body);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new ResponseDto<>(HttpStatus.INTERNAL_SERVER_ERROR, "application/json;charset=utf-8");
    }

    private static ResponseDto getStaticFile(String requestPath) {
        try {
            String contentType = "text/html;charset=utf-8";
            String filePath = "src/main/resources/templates";

            if (!requestPath.endsWith(".html")) {
                contentType = ResourceLoader.getContentType(requestPath);
                filePath = "src/main/resources/static";
            }

            byte[] body = Files.readAllBytes(new File(filePath + requestPath).toPath());

            return new ResponseDto<>(HttpStatus.OK, contentType, body);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new ResponseDto<>(HttpStatus.INTERNAL_SERVER_ERROR, "application/json;charset=utf-8");
    }

    private static ResponseDto signup(String requestPath) {
        String[] userArray = requestPath.split("\\?")[1].split("&");
        String userId = userArray[0];
        String password = userArray[1];
        String name = userArray[2];
        String email = userArray[3];

        User user = new User(userId, password, name, email);
        Database.addUser(user);

        return new ResponseDto<>(HttpStatus.FOUND, "application/json;charset=utf-8", "/index.html");
    }

}


package controller;

import dto.ResponseDto;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class HomeController {

    public ResponseDto route(String url) {
        if (url.equals("/") || url.equals("/index.html")) {
            return getIndex(url);
        }
        return getStatic(url);
    }

    private ResponseDto getIndex(String url) {
        String filePath = "src/main/resources/templates/index.html";

        try {
            byte[] body = Files.readAllBytes(new File(filePath).toPath());
            return new ResponseDto(url, 200, body);
        } catch (IOException e) {
            byte[] body = "404 Not Found".getBytes();
            return new ResponseDto(url, 404, body);
        }
    }

    private ResponseDto getStatic(String url) {
        String filePath = "src/main/resources/static" + url;

        try {
            byte[] body = Files.readAllBytes(new File(filePath).toPath());
            return new ResponseDto(url, 200, body);
        } catch (IOException e) {
            byte[] body = "404 Not Found".getBytes();
            return new ResponseDto(url, 404, body);
        }
    }
}

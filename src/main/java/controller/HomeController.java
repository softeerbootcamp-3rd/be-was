package controller;

import model.Response;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class HomeController implements Controller{

    public Response route(String url) {
        if (url.equals("/") || url.equals("/index.html")) {
            return getIndex(url);
        }
        return getStatic(url);
    }

    private Response getIndex(String url) {
        String filePath = "src/main/resources/templates/index.html";

        try {
            byte[] body = Files.readAllBytes(new File(filePath).toPath());
            return new Response(filePath, 200, body);
        } catch (IOException e) {
            byte[] body = "404 Not Found".getBytes();
            return new Response(filePath, 404, body);
        }
    }

    private Response getStatic(String url) {
        String filePath = "src/main/resources/static" + url;

        try {
            byte[] body = Files.readAllBytes(new File(filePath).toPath());
            return new Response(url, 200, body);
        } catch (IOException e) {
            byte[] body = "404 Not Found".getBytes();
            return new Response(url, 404, body);
        }
    }
}

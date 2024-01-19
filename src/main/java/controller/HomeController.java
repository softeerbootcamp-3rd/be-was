package controller;

import model.Response;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class HomeController implements Controller{

    public Response route(String url) {
        if (url.equals("/")) {
            return redirectIndex();
        }
        if (url.equals("/index.html")) {
            return getIndex(url);
        }
        return getStatic(url);
    }

    private Response redirectIndex() {
        return new Response(302, "/index.html");
    }

    private Response getIndex(String url) {
        String filePath = "src/main/resources/templates/index.html";

        try {
            byte[] body = Files.readAllBytes(new File(filePath).toPath());
            return new Response(200, url, body);
        } catch (IOException e) {
            byte[] body = "404 Not Found".getBytes();
            return new Response(404, body);
        }
    }

    private Response getStatic(String url) {
        String filePath = "src/main/resources/static" + url;

        try {
            byte[] body = Files.readAllBytes(new File(filePath).toPath());
            return new Response(200, url, body);
        } catch (IOException e) {
            byte[] body = "404 Not Found".getBytes();
            return new Response(404, body);
        }
    }
}

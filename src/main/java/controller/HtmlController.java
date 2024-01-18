package controller;

import request.HttpRequest;
import response.HttpResponse;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public class HtmlController implements Controller {
    @Override
    public void process(HttpRequest request, HttpResponse response) {
        String url = "src/main/resources/templates" + request.getUrl();

        File file = new File(url);
        if (file.isFile()) {
            try{
                byte[] body = Files.readAllBytes(new File(url).toPath());
                set200Response(response, body);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            set404Response(response);
        }

    }

    private static void set200Response(HttpResponse response, byte[] body) {
        response.setVersion("HTTP/1.1");
        response.setStatusCode("200");
        response.setStatusMessage("OK");
        response.setBody(body);
        response.getHeaders().put("Content-Type", "text/html;charset=utf-8");
        response.getHeaders().put("Content-Length", String.valueOf(body.length));
    }

    private static void set404Response(HttpResponse response) {
        response.setVersion("HTTP/1.1");
        response.setStatusCode("404");
        response.setStatusMessage("NOT FOUND");
        response.setBody("404 NOT FOUND".getBytes());
    }
}

package controller;

import request.HttpRequest;
import response.HttpResponse;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

public class HtmlController implements Controller {
    @Override
    public void process(HttpRequest request, HttpResponse response) {
        String url = "src/main/resources/templates" + request.getUrl();
        Map<String, String> headers = new HashMap<>();

        File file = new File(url);
        if (file.isFile()) {
            try{
                byte[] body = Files.readAllBytes(new File(url).toPath());

                headers.put("Content-Type", "text/html;charset=utf-8");
                headers.put("Content-Length", String.valueOf(body.length));
                response.setResponse("200", "OK", body, headers);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            headers.put("Content-Type", "text/html;charset=utf-8");
            response.setResponse("404", "NOT FOUND", "404 NOT FOUND".getBytes(), headers);
        }

    }

}

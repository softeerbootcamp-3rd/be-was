package controller;

import request.HttpRequest;
import response.HttpResponse;
import response.HttpResponseStatus;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

public class ResourceController implements Controller {

    static Map<String, String> contentType = new HashMap<>();

    static {
        contentType.put("html", "text/html; charset=utf-8");
        contentType.put("css", "text/css; charset=utf-8");
        contentType.put("js", "text/javascript; charset=utf-8");
        contentType.put("png", "image/png");
        contentType.put("jpg", "image/jpg");
        contentType.put("ico", "image/x-icon");
    }
    @Override
    public void process(HttpRequest request, HttpResponse response) {
        String url;
        Map<String, String> headers = new HashMap<>();
        if (request.getUrl().endsWith(".html")) {
            url = "src/main/resources/templates" + request.getUrl();
        } else {
            url = "src/main/resources/static" + request.getUrl();
        }
        File file = new File(url);
        if (file.isFile()) {
            try{
                byte[] body = Files.readAllBytes(new File(url).toPath());

                headers.put("Content-Type", getContentType(url));
                headers.put("Content-Length", String.valueOf(body.length));
                response.setResponse(HttpResponseStatus.OK, body, headers);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            headers.put("Content-Type", "text/html;charset=utf-8");
            response.setResponse(HttpResponseStatus.NOT_FOUND, "404 NOT FOUND".getBytes(), headers);
        }

    }

    private String getContentType(String url) {
        String extension = url.substring(url.lastIndexOf(".") + 1);
        String result = contentType.get(extension);
        if (result == null) {
            return "font/" + extension;
        }
        return result;
    }
}

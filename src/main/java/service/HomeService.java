package service;

import httpmessage.Request.HttpRequest;
import httpmessage.Response.HttpResponse;
import httpmessage.Response.ResponsePasing;
import httpmessage.View.View;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public class HomeService implements Service {
    /*
    private byte[] body;
    DataOutputStream dos;
    int statusCode;
    String URL;
    String ContentType;
    */

    public void process(HttpRequest httpRequest, HttpResponse httpResponse) throws IOException {
        ResponsePasing responsePasing = new ResponsePasing();

        String path =  httpRequest.getPath();
        String httpMethod = httpRequest.getHttpMethod();
        String url = responsePasing.getUrl(path);
        String contentType = responsePasing.getContentType(path);
        String statusLine;
        int statusCode;
        byte[] body;

        if (url.contains("404")) {
            statusCode = 404;
            String errorPageContent = "<html><head><title>404 Not Found</title></head><body><h1>404 Not Found</h1></body></html>";
            statusLine = responsePasing.getSatusCode(statusCode);
            body = errorPageContent.getBytes(StandardCharsets.UTF_8);
        }
        else {
            statusCode = 200;
            statusLine = responsePasing.getSatusCode(statusCode);
            body = Files.readAllBytes(new File(url).toPath());
        }
        httpResponse.setHttpResponse(body,contentType,statusCode,statusLine);
    }
}

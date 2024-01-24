package controller;

import httpmessage.request.HttpRequest;
import httpmessage.response.HttpResponse;
import httpmessage.response.ResponsePasing;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;

public class HomeController implements Controller {
    public void service(HttpRequest httpRequest, HttpResponse httpResponse) throws IOException {
        ResponsePasing responsePasing = new ResponsePasing();

        String path =  httpRequest.getPath();
        String url = responsePasing.getUrl(path);
        String contentType = responsePasing.getContentType(path);
        String statusLine;
        int statusCode;
        byte[] body;

        if (url.contains("404")) {
            statusCode = 404;
            String errorPageContent = "<html><head><title>404 Not Found</title></head><body><h1>404 Not Found</h1></body></html>";
            statusLine = responsePasing.getSatusCode(statusCode);
            body = errorPageContent.getBytes("UTF-8");
        }
        else {
            statusCode = 200;
            statusLine = responsePasing.getSatusCode(statusCode);
            FileInputStream fis = new FileInputStream(url);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();

            byte[] buffer = new byte[4096];
            int bytesRead;

            while ((bytesRead = fis.read(buffer)) != -1) {
                bos.write(buffer, 0, bytesRead);
            }
            body = bos.toByteArray();
        }

        httpResponse.setHttpResponse(body,contentType,statusCode,statusLine);

    }
}

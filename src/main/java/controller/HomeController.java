package controller;

import httpmessage.ContentType;
import httpmessage.HttpStatusCode;
import httpmessage.request.HttpRequest;
import httpmessage.response.HttpResponse;
import httpmessage.response.FilePathContent;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class HomeController implements Controller {
    public void service(HttpRequest httpRequest, HttpResponse httpResponse) throws IOException {
        FilePathContent filePathContent = new FilePathContent();

        String filePath =  filePathContent.getFilePath(httpRequest.getPath());
        String contentType = ContentType.getContentType(httpRequest.getPath());
        String statusLine;
        int statusCode;
        byte[] body;

        statusCode = 200;
        statusLine = HttpStatusCode.findBy(statusCode);

        FileInputStream fis = new FileInputStream(filePath);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        byte[] buffer = new byte[4096];
        int bytesRead;

        while ((bytesRead = fis.read(buffer)) != -1) {
            bos.write(buffer, 0, bytesRead);
        }
        body = bos.toByteArray();
        httpResponse.setHttpResponse(body,contentType,statusCode,statusLine);
    }
}

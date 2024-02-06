package controller;

import httpmessage.ContentType;
import httpmessage.HttpStatusCode;
import httpmessage.request.HttpRequest;
import httpmessage.response.HttpResponse;
import httpmessage.response.FilePathContent;

import java.io.*;

public class HomeController implements Controller {
    public void service(HttpRequest httpRequest, HttpResponse httpResponse) throws IOException {
        FilePathContent filePathContent = new FilePathContent();
        HttpStatusCode httpStatusCode;
        String filePath =  filePathContent.getFilePath(httpRequest.getPath());
        String contentType = ContentType.getContentType(httpRequest.getPath());
        String statusLine;
        int statusCode;
        byte[] body;

        File file = new File(filePath);

        if (!file.exists() || file.isDirectory()) {
            // 파일이 존재하지 않거나 디렉토리인 경우 404 상태 코드 반환
            httpStatusCode = HttpStatusCode.NOT_FOUND;
            body = "404 Not Found".getBytes();
        } else {
            // 파일이 존재하는 경우 정상적으로 읽어옴
            httpStatusCode = HttpStatusCode.OK;

            try (FileInputStream fis = new FileInputStream(file);
                 ByteArrayOutputStream bos = new ByteArrayOutputStream()) {

                byte[] buffer = new byte[4096];
                int bytesRead;

                while ((bytesRead = fis.read(buffer)) != -1) {
                    bos.write(buffer, 0, bytesRead);
                }
                body = bos.toByteArray();
            }
        }
        httpResponse.setHttpResponse(body,contentType,httpStatusCode);
    }
}

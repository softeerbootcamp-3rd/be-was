package controller;

import httpmessage.ContentType;
import httpmessage.HttpStatusCode;
import httpmessage.request.HttpRequest;
import httpmessage.response.FilePathContent;
import httpmessage.response.HttpResponse;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class WritingDetailController implements Controller{
    public void service(HttpRequest httpRequest, HttpResponse httpResponse) throws IOException {
        httpResponse.setHttpStatusCode(HttpStatusCode.MOVED_TEMPORARILY);
        String id = httpRequest.getPath().split("\\?")[1];

        if (!httpRequest.getCookie().isEmpty()) {
            httpResponse.setRedirectionPath("/qna/show.html?"+id);
        }

        else{
            httpResponse.setRedirectionPath("/user/login.html");
        }

    }
}

package controller;

import annotation.Controller;
import annotation.RequestMapping;
import constant.HttpHeader;
import constant.HttpStatus;
import constant.MimeType;
import util.HtmlBuilder;
import util.ResourceLoader;
import util.SessionManager;
import webserver.HttpRequest;
import webserver.HttpResponse;

import java.io.IOException;

@Controller
public class ViewController {

    @RequestMapping(method = "GET", path = "/user/list")
    public static HttpResponse userList(HttpRequest request) throws IOException {
        if (!SessionManager.isLoggedIn(request))
            return HttpResponse.builder()
                    .status(HttpStatus.FOUND)
                    .addHeader(HttpHeader.LOCATION, "/index.html")
                    .build();

        byte[] fileContent = ResourceLoader.getFileContent("/user/list.html");
        return HttpResponse.builder()
                .status(HttpStatus.OK)
                .addHeader(HttpHeader.CONTENT_TYPE, MimeType.HTML.getMimeType())
                .body(HtmlBuilder.process(request, fileContent))
                .build();
    }
}

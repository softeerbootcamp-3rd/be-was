package controller;

import annotation.Controller;
import annotation.RequestMapping;
import constant.HttpHeader;
import constant.HttpStatus;
import constant.MimeType;
import util.html.HtmlBuilder;
import util.web.ResourceLoader;
import util.web.SharedData;
import webserver.HttpResponse;

import java.io.IOException;

@Controller
public class ViewController {

    @RequestMapping(method = "GET", path = "/user/list")
    public static HttpResponse userList() throws IOException {
        if (SharedData.requestUser.get() == null)
            return HttpResponse.builder()
                    .status(HttpStatus.FOUND)
                    .addHeader(HttpHeader.LOCATION, "/index.html")
                    .build();

        byte[] fileContent = ResourceLoader.getFileContent("/user/list.html");
        return HttpResponse.builder()
                .status(HttpStatus.OK)
                .addHeader(HttpHeader.CONTENT_TYPE, MimeType.HTML.getMimeType())
                .body(HtmlBuilder.process(fileContent))
                .build();
    }
}

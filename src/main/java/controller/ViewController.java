package controller;

import annotation.Controller;
import annotation.RequestMapping;
import annotation.RequestParam;
import constant.HttpHeader;
import constant.HttpStatus;
import constant.MimeType;
import db.QnaDatabase;
import exception.ResourceNotFoundException;
import model.Qna;
import util.HtmlBuilder;
import util.ResourceLoader;
import util.SharedData;
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

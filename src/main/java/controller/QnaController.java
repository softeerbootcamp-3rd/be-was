package controller;

import annotation.Controller;
import annotation.RequestBody;
import annotation.RequestMapping;
import constant.HttpHeader;
import constant.HttpStatus;
import db.QnaDatabase;
import dto.QnaDto;
import model.Qna;
import model.User;
import util.SharedData;
import webserver.HttpResponse;

import java.util.Date;

@Controller
public class QnaController {

    @RequestMapping(method = "POST", path = "/qna/post")
    public static HttpResponse postQna(@RequestBody QnaDto qna) {
        User currentUser = SharedData.requestUser.get();
        if (currentUser == null)
            return HttpResponse.builder()
                    .status(HttpStatus.FORBIDDEN)
                    .body(HttpStatus.FORBIDDEN.getFullMessage())
                    .build();

        QnaDatabase.add(new Qna(currentUser.getUserId(), qna.getTitle(), qna.getContents(), new Date()));
        return HttpResponse.builder()
                .status(HttpStatus.FOUND)
                .addHeader(HttpHeader.LOCATION, "/index.html")
                .build();
    }
}

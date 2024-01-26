package controller;

import annotation.Controller;
import annotation.RequestBody;
import annotation.RequestMapping;
import constant.HttpStatus;
import dto.QnaDto;
import util.SharedData;
import webserver.HttpResponse;

@Controller
public class QnaController {

//    @RequestMapping(method = "POST", path = "/qna/post")
//    public static HttpResponse postQna(@RequestBody QnaDto qna) {
//        if (SharedData.requestUser.get() == null)
//            return HttpResponse.builder()
//                    .status(HttpStatus.FORBIDDEN)
//                    .body(HttpStatus.FORBIDDEN.getFullMessage())
//                    .build();
//
//    }
}

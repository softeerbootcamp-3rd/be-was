package controller;

import config.HTTPRequest;
import config.HTTPResponse;
import db.Database;
import model.Qna;


import static webserver.RequestHandler.threadUuid;

public class QnaController {

    //QNA 생성
    public static HTTPResponse createQna(HTTPRequest request) {

        if(threadUuid.get() == null)
            return PageController.RedirectStaticPage("/user/login.html");

        Qna qna = parseToQna(request);
        Database.addQna(qna);

        return PageController.RedirectStaticPage("/index.html");

    }
    // request body에서 Qna를 파싱
    private static Qna parseToQna(HTTPRequest request){

        String title = null;
        String writer = null;
        String content = null;

        title = request.getBody().get("title");
        writer = request.getBody().get("writer");
        content = request.getBody().get("contents");


        return new Qna(writer,title,content);
    }
}


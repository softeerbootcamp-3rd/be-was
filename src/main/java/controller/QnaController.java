package controller;

import config.HTTPRequest;
import config.HTTPResponse;
import db.Database;
import model.Qna;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static webserver.RequestHandler.threadUuid;

public class QnaController {
    public static HTTPResponse createQna(HTTPRequest request) {

        if(threadUuid == null)
            return PageController.RedirectStaticPage("/user/login.html");

        HTTPResponse response = null;

        Qna qna = parseToQna(request);
        Database.addQna(qna);




        return response;
    }
    private static Qna parseToQna(HTTPRequest request){
        List<String> bodyStrings = new ArrayList<>(Arrays.asList(request.getBody().toString().split("&")));
        String title = null;
        String writer = null;
        String content = null;

        for(String str : bodyStrings){
            if(str.contains("writer"))
                writer = str.substring("writer=".length());
            else if(str.contains("title"))
                writer = str.substring("title=".length());
            else if(str.contains("content"))
                writer = str.substring("content=".length());
        }

        return new Qna(writer,title,content);
    }
}


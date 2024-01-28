package controller;

import annotation.Controller;
import annotation.RequestBody;
import annotation.RequestMapping;
import com.google.common.base.Strings;
import constant.HttpHeader;
import constant.HttpStatus;
import db.CommentDatabase;
import db.QnaDatabase;
import dto.QnaAnswerDto;
import dto.QnaDto;
import model.Comment;
import model.Qna;
import model.User;
import util.web.SharedData;
import webserver.HttpResponse;

import java.util.Date;

@Controller
public class QnaController {

    @RequestMapping(method = "POST", path = "/qna/post")
    public static HttpResponse postQna(@RequestBody QnaDto qna) {
        User currentUser = SharedData.requestUser.get();
        if (currentUser == null)
            return HttpResponse.of(HttpStatus.FORBIDDEN);

        QnaDatabase.add(new Qna(currentUser.getUserId(), qna.getTitle(), qna.getContents(), new Date()));
        return HttpResponse.builder()
                .status(HttpStatus.FOUND)
                .addHeader(HttpHeader.LOCATION, "/index.html")
                .build();
    }

    @RequestMapping(method = "POST", path = "/questions/{qnaId}/answer")
    public static HttpResponse postAnswer(@RequestBody QnaAnswerDto answer) {
        User currentUser = SharedData.requestUser.get();
        if (currentUser == null)
            return HttpResponse.of(HttpStatus.FORBIDDEN);

        String qnaIdString = SharedData.pathParams.get().get("qnaId");
        if (Strings.isNullOrEmpty(qnaIdString))
            return HttpResponse.of(HttpStatus.BAD_REQUEST);

        Long qnaId = Long.valueOf(qnaIdString);
        CommentDatabase.add(new Comment(qnaId, currentUser.getUserId(), answer.getContent(), new Date()));
        return HttpResponse.builder()
                .status(HttpStatus.FOUND)
                .addHeader(HttpHeader.LOCATION, "/qna/show.html?qnaId=" + qnaIdString)
                .build();
    }

    @RequestMapping(method = "POST", path = "/questions/{qnaId}/delete")
    public static HttpResponse deleteQna() {
        String qnaIdString = SharedData.pathParams.get().get("qnaId");
        if (Strings.isNullOrEmpty(qnaIdString))
            return HttpResponse.of(HttpStatus.BAD_REQUEST);
        Long qnaId = Long.valueOf(qnaIdString);
        Qna qna = QnaDatabase.findById(qnaId);

        User currentUser = SharedData.requestUser.get();
        if (currentUser == null || !currentUser.getUserId().equals(qna.getWriterId()))
            return HttpResponse.of(HttpStatus.FORBIDDEN);

        QnaDatabase.deleteById(qnaId);
        return HttpResponse.builder()
                .status(HttpStatus.FOUND)
                .addHeader(HttpHeader.LOCATION, "/index.html")
                .build();
    }
}

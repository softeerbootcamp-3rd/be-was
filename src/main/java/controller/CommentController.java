package controller;

import annotation.Controller;
import annotation.RequestMapping;
import com.google.common.base.Strings;
import constant.HttpStatus;
import database.CommentRepository;
import entity.Comment;
import entity.User;
import model.SharedData;
import webserver.HttpResponse;

import java.util.Objects;

@Controller
public class CommentController {

    @RequestMapping(method = "POST", path = "/comments/{commentId}/delete")
    public static HttpResponse deleteComment() {
        String commentIdString = SharedData.pathParams.get().get("commentId");
        if (Strings.isNullOrEmpty(commentIdString))
            return HttpResponse.of(HttpStatus.BAD_REQUEST);
        Long commentId = Long.valueOf(commentIdString);
        Comment comment = CommentRepository.findById(commentId);

        User currentUser = SharedData.requestUser.get();
        if (currentUser == null || !Objects.equals(currentUser.getUserId(), comment.getWriterId()))
            return HttpResponse.of(HttpStatus.FORBIDDEN);

        CommentRepository.deleteById(commentId);
        return HttpResponse.redirect("/board/show.html?boardId=" + comment.getBoardId());
    }
}

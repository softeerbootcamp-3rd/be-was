package controller;

import annotation.Controller;
import annotation.RequestMapping;
import com.google.common.base.Strings;
import constant.HttpHeader;
import constant.HttpStatus;
import db.CommentDatabase;
import model.Comment;
import model.User;
import util.web.SharedData;
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
        Comment comment = CommentDatabase.findById(commentId);

        User currentUser = SharedData.requestUser.get();
        if (currentUser == null || !Objects.equals(currentUser.getUserId(), comment.getWriterId()))
            return HttpResponse.of(HttpStatus.FORBIDDEN);

        CommentDatabase.deleteById(commentId);
        return HttpResponse.redirect("/post/show.html?postId=" + comment.getPostId());
    }
}

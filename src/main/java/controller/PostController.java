package controller;

import annotation.Controller;
import annotation.RequestBody;
import annotation.RequestMapping;
import com.google.common.base.Strings;
import constant.HttpHeader;
import constant.HttpStatus;
import db.CommentDatabase;
import db.PostDatabase;
import dto.CommentDto;
import dto.PostDto;
import model.Comment;
import model.Post;
import model.User;
import util.web.SharedData;
import webserver.HttpResponse;

import java.util.Date;
import java.util.Objects;

@Controller
public class PostController {

    @RequestMapping(method = "POST", path = "/post")
    public static HttpResponse postPost(@RequestBody PostDto post) {
        User currentUser = SharedData.requestUser.get();
        if (currentUser == null)
            return HttpResponse.of(HttpStatus.FORBIDDEN);

        PostDatabase.add(new Post(currentUser.getUserId(), post.getTitle(), post.getContents(), new Date()));
        return HttpResponse.builder()
                .status(HttpStatus.FOUND)
                .addHeader(HttpHeader.LOCATION, "/index.html")
                .build();
    }

    @RequestMapping(method = "POST", path = "/post/{postId}/comment")
    public static HttpResponse postComment(@RequestBody CommentDto comment) {
        User currentUser = SharedData.requestUser.get();
        if (currentUser == null)
            return HttpResponse.of(HttpStatus.FORBIDDEN);

        String postIdString = SharedData.pathParams.get().get("postId");
        if (Strings.isNullOrEmpty(postIdString))
            return HttpResponse.of(HttpStatus.BAD_REQUEST);

        Long postId = Long.valueOf(postIdString);
        CommentDatabase.add(new Comment(postId, currentUser.getUserId(), comment.getContent(), new Date()));
        return HttpResponse.builder()
                .status(HttpStatus.FOUND)
                .addHeader(HttpHeader.LOCATION, "/post/show.html?postId=" + postIdString)
                .build();
    }

    @RequestMapping(method = "POST", path = "/questions/{postId}/delete")
    public static HttpResponse deletePost() {
        String postIdString = SharedData.pathParams.get().get("postId");
        if (Strings.isNullOrEmpty(postIdString))
            return HttpResponse.of(HttpStatus.BAD_REQUEST);
        Long postId = Long.valueOf(postIdString);
        Post post = PostDatabase.findById(postId);

        User currentUser = SharedData.requestUser.get();
        if (currentUser == null || !Objects.equals(currentUser.getUserId(), post.getWriterId()))
            return HttpResponse.of(HttpStatus.FORBIDDEN);

        PostDatabase.deleteById(postId);
        CommentDatabase.deleteByPostId(postId);
        return HttpResponse.builder()
                .status(HttpStatus.FOUND)
                .addHeader(HttpHeader.LOCATION, "/index.html")
                .build();
    }

}

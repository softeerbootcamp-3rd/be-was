package controller;

import annotation.Controller;
import annotation.RequestBody;
import annotation.RequestMapping;
import com.google.common.base.Strings;
import constant.HttpStatus;
import database.CommentRepository;
import database.ImageRepository;
import database.PostRepository;
import dto.CommentDto;
import dto.PostDto;
import model.Comment;
import model.Image;
import model.Post;
import model.User;
import util.web.SharedData;
import webserver.HttpResponse;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.Objects;

@Controller
public class PostController {

    @RequestMapping(method = "POST", path = "/post")
    public static HttpResponse postPost(@RequestBody PostDto post) {
        User currentUser = SharedData.requestUser.get();
        if (currentUser == null)
            return HttpResponse.of(HttpStatus.FORBIDDEN);

        Long postId = PostRepository.add(new Post(currentUser.getUserId(), post.getTitle(), post.getContents(), new Date()));
        if (post.getImage() != null) {
            ImageRepository.add(new Image(postId, "png", post.getImage().getData()));
        }
        return HttpResponse.redirect("/index.html");
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
        CommentRepository.add(new Comment(postId, currentUser.getUserId(), comment.getContent(), new Date()));
        return HttpResponse.redirect("/post/show.html?postId=" + postIdString);
    }

    @RequestMapping(method = "POST", path = "/post/{postId}/delete")
    public static HttpResponse deletePost() {
        String postIdString = SharedData.pathParams.get().get("postId");
        if (Strings.isNullOrEmpty(postIdString))
            return HttpResponse.of(HttpStatus.BAD_REQUEST);
        Long postId = Long.valueOf(postIdString);
        Post post = PostRepository.findById(postId);

        User currentUser = SharedData.requestUser.get();
        if (currentUser == null || !Objects.equals(currentUser.getUserId(), post.getWriterId()))
            return HttpResponse.of(HttpStatus.FORBIDDEN);

        PostRepository.deleteById(postId);
        ImageRepository.deleteByPostId(postId);
        CommentRepository.deleteByPostId(postId);
        return HttpResponse.redirect("/index.html");
    }

}

package controller;

import annotation.GetMapping;
import annotation.PostMapping;
import annotation.RequestParam;
import db.Database;
import model.Post;
import request.HttpRequest;
import session.SessionManager;

import java.time.LocalDateTime;

public class PostController {
    SessionManager sessionManager = new SessionManager();
    private static Long sequence = 0L;

    @GetMapping(url = "/post")
    public String getWriteForm(HttpRequest request) {
        if (sessionManager.getUserBySessionId(request) == null) {
            return "/user/login.html";
        } else {
            return "write_form.html";
        }
    }

    @PostMapping(url = "/post")
    public String createPost(@RequestParam(name = "writer") String writer,
                             @RequestParam(name = "title") String title,
                             @RequestParam(name = "contents") String contents) {
        Post post = new Post(writer, title, contents);
        post.setId(++sequence);
        post.setCreateTime(LocalDateTime.now());
        Database.addPost(post);
        return "/index.html";
    }
}

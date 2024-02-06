package controller;

import annotation.GetMapping;
import annotation.PostMapping;
import annotation.RequestParam;
import db.Database;
import model.Post;
import model.User;
import request.HttpRequest;
import response.HttpResponse;
import response.HttpResponseBuilder;
import response.HttpResponseStatus;
import session.SessionManager;
import view.ViewResolver;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

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

    @GetMapping(url = "/post/detail")
    public HttpResponse showDetail(HttpRequest request) {
        Map<String, String> responseHeaders = new HashMap<>();

        Map<String, Object> model = new HashMap<>();

        if(!request.isAuth()) {
            HttpResponse response = new HttpResponse();
            responseHeaders.put("location", "/user/login.html");
            response.setResponse(HttpResponseStatus.FOUND, null, responseHeaders);
            return response;
        } else {
            User loginUser = sessionManager.getUserBySessionId(request);
            model.put("name", loginUser.getName());

            String id = request.getParams().get("id");
            model.put("postId", id);
            byte[] body = ViewResolver.resolve("/post_detail.html", request.isAuth(), model);
            responseHeaders.put("Content-Type", "text/html; charset=utf-8");
            responseHeaders.put("Content-Length", String.valueOf(body.length));
            return new HttpResponseBuilder().status(HttpResponseStatus.OK)
                    .headers(responseHeaders)
                    .body(body)
                    .build();
        }
    }
}

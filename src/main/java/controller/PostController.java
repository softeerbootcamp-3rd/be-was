package controller;

import http.HttpStatus;
import http.response.HttpResponse;
import model.Post;
import service.PostService;
import utils.Parser;

import java.util.Date;
import java.util.Map;
import java.util.UUID;

public class PostController {
    private static final PostService postService = new PostService();

    public static HttpResponse upload(String body) {
        Map<String, String> bodyMap = Parser.extractParams(body);
        String postId = UUID.randomUUID().toString();
        Post post = new Post(postId, bodyMap.get("writer"), bodyMap.get("title"), bodyMap.get("contents"), new Date());

        postService.createPost(post);
        Map<String, String> header = Map.of("Location", "/index.html");
        return HttpResponse.of(HttpStatus.REDIRECT, header);
    }
}

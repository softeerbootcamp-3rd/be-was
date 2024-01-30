package controller;

import http.HttpStatus;
import http.request.HttpRequest;
import http.request.MultiPartForm;
import http.response.HttpResponse;
import model.Post;
import service.PostService;

import java.util.Date;
import java.util.Map;
import java.util.UUID;

public class PostController {
    private static final PostService postService = new PostService();

    public static HttpResponse upload(HttpRequest request) {
        MultiPartForm multiPartForm = request.getMultiPartForm();
        Map<String, String> fields = multiPartForm.getFields();
        String postId = UUID.randomUUID().toString();
        Post post = new Post(postId, fields.get("writer"), fields.get("title"), fields.get("contents"), new Date(), fields.get("file"));

        postService.createPost(post);
        Map<String, String> header = Map.of("Location", "/index.html");
        return HttpResponse.of(HttpStatus.REDIRECT, header);
    }
}

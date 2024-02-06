package controller;

import http.HttpStatus;
import http.request.HttpRequest;
import http.request.MultiPartForm;
import http.response.HttpResponse;
import model.Post;
import service.PostService;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

public class PostController {
    private static final PostService postService = new PostService();

    public static HttpResponse upload(HttpRequest request) {
        MultiPartForm multiPartForm = request.getMultiPartForm();
        Map<String, String> fields = multiPartForm.getFields();
        String postId = UUID.randomUUID().toString();

        // 현재 시각을 yyyy-MM-dd HH:mm:ss 형식으로 포맷팅
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDate = dateFormat.format(new Date());
        Post post = new Post(postId, fields.get("writer"), fields.get("title"), fields.get("contents"), formattedDate, fields.get("file"));

        postService.createPost(post);
        Map<String, String> header = Map.of("Location", "/index.html");
        return HttpResponse.of(HttpStatus.REDIRECT, header);
    }
}

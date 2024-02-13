package controller;

import http.annotation.PostMapping;
import http.request.HttpRequest;
import http.response.HttpResponse;
import model.Post;
import service.HomePageService;
import db.Database;


import java.io.IOException;

public class PostController {
    private final HomePageService homePageService;

    public PostController(HomePageService homePageService) {
        this.homePageService = homePageService;
    }

    @PostMapping("/qna/write")
    public void handleWriteRequest(HttpRequest request, HttpResponse response) {
        // 게시글 데이터 추출 및 저장
        String writer = request.getBodyParams().get("writer");
        String title = request.getBodyParams().get("title");
        String contents = request.getBodyParams().get("contents");

        Post post = new Post(writer, title, contents);
        Database.addPost(post);

        // 홈페이지 HTML 업데이트
        try {
            homePageService.getUpdatedHomePage();
        } catch (IOException e) {
            // 에러 처리
        }

        // 홈페이지로 리디렉션
        response.setStatusCode(302); // HTTP 상태 코드를 302(Redirect)로 설정
        response.setHeader("Location", "/"); // 홈페이지 ('/')로 리디렉션
    }
}

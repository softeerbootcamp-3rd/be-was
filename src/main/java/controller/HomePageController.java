package controller;

import service.HomePageService;
import http.annotation.GetMapping;
import http.request.HttpRequest;
import http.response.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HomePageController {
    private static final Logger logger = LoggerFactory.getLogger(HomePageController.class);
    private final HomePageService homePageService;

    public HomePageController(HomePageService homePageService) {
        this.homePageService = homePageService;
    }

    @GetMapping("/")
    public void handleHomePage(HttpRequest request, HttpResponse response) {
        try {
            String pageContent = homePageService.getHomePageHtml(); // 수정된 메서드 호출
            response.setBody(pageContent.getBytes());
            response.setHeader("Content-Type", "text/html; charset=utf-8");
            response.setStatusCode(302);
        } catch (Exception e) {
            logger.error("Error handling home page request", e);
            response.setStatusCode(500); // 내부 서버 오류
            response.setBody("Internal Server Error".getBytes());
        }
    }
}


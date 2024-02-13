package service;

import db.Database;
import model.Post;

import java.io.IOException;
import java.util.List;

public class HomePageService {

    public String getHomePageHtml() {
        return Database.getHomePageHtml(); // 데이터베이스에서 홈페이지 HTML을 가져옵니다.
    }

    // HTML 콘텐츠를 업데이트하고 반환하는 public 메서드
    public String getUpdatedHomePage() throws IOException {
        String updatedHtml = createHomePage();
        Database.setHomePageHtml(updatedHtml);
        return updatedHtml;
    }

    private String createHomePage() throws IOException {
        String templateHtml = Database.getHomePageHtml();
        StringBuilder postsHtml = new StringBuilder();

        List<Post> posts = Database.getAllPosts();
        for (Post post : posts) {
            postsHtml.append("<li><div class=\"wrap\"><div class=\"main\">")
                    .append("<strong class=\"subject\"><a href=\"./qna/show.html\">")
                    .append(post.getTitle()).append("</a></strong>")
                    .append("<div class=\"auth-info\">")
                    .append("<i class=\"icon-add-comment\"></i>")
                    .append("<span class=\"time\">").append(post.getCreateTime()).append("</span>")
                    .append("<a href=\"./user/profile.html\" class=\"author\">").append(post.getWriter()).append("</a>")
                    .append("</div></div></li>");
        }

        return templateHtml.replace("<!-- Posts -->", postsHtml.toString());
    }
}

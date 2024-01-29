package model;

import httpmessage.HttpSession;
import httpmessage.request.HttpRequest;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

public class Article {
    String title = "제목";
    String contents = "내용";

    String userId = "이대산";

    String createdate = "2024.1.29";
    String filePath;

    public Article(){};
    public Article(HttpRequest httpRequest){
        Map<String,String> article = httpRequest.getParmeter();

        System.out.println(article.get("filePath"));
        /*
        title = article.get("title");
        contents = article.get("contents");

        LocalDateTime createDate = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        createdate = createDate.format(formatter);

        String sid = httpRequest.getCookie();
        userId = new HttpSession().getUser(sid).getUserId();

        System.out.println(title+contents+createdate+userId);
        */
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public void setCreatedate(String createdate) {
        this.createdate = createdate;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContents() {
        return contents;
    }
    public String getCreatedate() {
        return createdate;
    }

    public String getTitle() {
        return title;
    }

    public String getUserId() {
        return userId;
    }

}

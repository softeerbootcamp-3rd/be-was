package model;

import db.Database;

import java.time.LocalDateTime;
import java.util.Deque;
import java.util.LinkedList;

public class Qna {
    private String writer;
    private String title;
    private String content;
    private LocalDateTime creationTime;



    public Qna(String writer, String title, String content) {
        this.writer = writer;
        this.title = title;
        this.content = content;
        this.creationTime = LocalDateTime.now();
    }

    public String getWriter() {
        return writer;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }
    public LocalDateTime getCreationTime() {
        return creationTime;
    }


    public StringBuilder toThumbnail(){

        StringBuilder sb = new StringBuilder("");
        sb.append("<li>" +
                "<div class=\"wrap\">" +
                "<div class=\"main\">" +
                "<strong class=\"subject\">" +
                "<a href=\"./qna/show.html\">");

        sb.append(title);
        sb.append("</a>" +
                "</strong>" +
                "<div class=\"auth-info\">" +
                "<i class=\"icon-add-comment\"></i>" +
                "<span class=\"time\">");

        sb.append(creationTime.toString());
        sb.append("</span>" +
                "<a href=\"./user/profile.html\" class=\"author\">");

        sb.append(writer);
        sb.append("</a>" +
                "</div>" +
                "</div>" +
                "</div>" +
                "</li>");

        return sb;
    }



}

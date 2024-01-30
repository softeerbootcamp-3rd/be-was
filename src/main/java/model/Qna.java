package model;

import db.Database;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class Qna {
    private Long id = 0L;
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

        StringBuilder sb = new StringBuilder();
        sb.append("              <li>\n" +
                "                  <div class=\"wrap\">\n" +
                "                      <div class=\"main\">\n" +
                "                          <strong class=\"subject\">\n" +
                "                              <a href=\"./qna/show.html/");

        sb.append(id);
        sb.append("\">");

        sb.append(title);
        sb.append("</a>\n" +
                "                          </strong>\n" +
                "                          <div class=\"auth-info\">\n" +
                "                              <i class=\"icon-add-comment\"></i>\n" +
                "                              <span class=\"time\">");

        String parsedLocalDateTimeNow = creationTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        sb.append(parsedLocalDateTimeNow);
        sb.append("</span>\n" +
                "                              <a href=\"./user/profile.html\" class=\"author\">");

        sb.append(writer);
        sb.append("</a>\n" +
                "                          </div>\n" +
                "                          <div class=\"reply\" title=\"댓글\">\n" +
                "                              <i class=\"icon-reply\"></i>\n" +
                "                              <span class=\"point\">0</span>\n" +
                "                          </div>\n" +
                "                      </div>\n" +
                "                  </div>\n" +
                "              </li>");

        return sb;
    }



}

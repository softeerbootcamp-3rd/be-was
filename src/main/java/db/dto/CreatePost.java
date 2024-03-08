package db.dto;

public class CreatePost {
    private String writer;
    private String title;
    private String content;
    public String getWriter() {
        return writer;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public CreatePost(String writer, String title, String content) {
        this.writer = writer;
        this.title = title;
        this.content = content;
    }
}

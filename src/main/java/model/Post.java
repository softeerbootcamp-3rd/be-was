package model;

public class Post {
    private Long postId;
    private String writer;
    private String title;
    private String contents;

    public Post(String writer, String title, String contents) {
        this.writer = writer;
        this.title = title;
        this.contents = contents;
    }

    public Post(Long postId, Post post) {
        this.postId = postId;
        this.writer = post.getWriter();
        this.title = post.getTitle();
        this.contents = post.getContents();
    }

    public Long getPostId() {
        return postId;
    }

    public String getWriter() {
        return writer;
    }

    public String getTitle() {
        return title;
    }

    public String getContents() {
        return contents;
    }
}

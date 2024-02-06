package model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.Objects;

public class Post {

    private Long id;
    private String writerId;
    private String title;
    private String contents;
    private Date createDatetime;

    public Post() {}

    public Post(Long id, String writerId, String title, String contents, Date createDatetime) {
        this.id = id;
        this.writerId = writerId;
        this.title = title;
        this.contents = contents;
        this.createDatetime = createDatetime;
    }

    public Post(String writerId, String title, String contents, Date createDatetime) {
        this.writerId = writerId;
        this.title = title;
        this.contents = contents;
        this.createDatetime = createDatetime;
    }

    public Long getId() {
        return id;
    }

    public String getWriterId() {
        return writerId;
    }

    public String getTitle() {
        return title;
    }

    public String getContents() {
        return contents;
    }

    public Date getCreateDatetime() {
        return createDatetime;
    }

    @Override
    public String toString() {
        return "Post{" +
                "id=" + id +
                ", writerId='" + writerId + '\'' +
                ", title='" + title + '\'' +
                ", content='" + contents + '\'' +
                ", createDatetime=" + createDatetime +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Post post = (Post) o;
        return Objects.equals(id, post.id) && Objects.equals(writerId, post.writerId) && Objects.equals(title, post.title)
                && Objects.equals(contents, post.contents) && Objects.equals(createDatetime, post.createDatetime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, writerId, title, contents, createDatetime);
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setWriterId(String writerId) {
        this.writerId = writerId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public void setCreateDatetime(Date createDatetime) {
        this.createDatetime = createDatetime;
    }

    public static Post of(ResultSet resultSet) throws SQLException {
        Post post = new Post();
        post.id = resultSet.getLong("id");
        post.writerId = resultSet.getString("writerId");
        post.title = resultSet.getString("title");
        post.contents = resultSet.getString("contents");
        post.createDatetime = resultSet.getTimestamp("createDatetime");
        return post;
    }
}

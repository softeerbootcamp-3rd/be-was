package model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.Objects;

public class Comment {

    private Long id;
    private Long postId;
    private String writerId;
    private String contents;
    private Date createDatetime;

    public Comment(Long id, Long postId, String writerId, String contents, Date createDatetime) {
        this.id = id;
        this.postId = postId;
        this.writerId = writerId;
        this.contents = contents;
        this.createDatetime = createDatetime;
    }

    public Comment(Long postId, String writerId, String contents, Date createDatetime) {
        this.postId = postId;
        this.writerId = writerId;
        this.contents = contents;
        this.createDatetime = createDatetime;
    }

    public Comment() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    public String getWriterId() {
        return writerId;
    }

    public void setWriterId(String writerId) {
        this.writerId = writerId;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public Date getCreateDatetime() {
        return createDatetime;
    }

    public void setCreateDatetime(Date createDatetime) {
        this.createDatetime = createDatetime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Comment comment = (Comment) o;
        return Objects.equals(id, comment.id) && Objects.equals(postId, comment.postId) && Objects.equals(writerId, comment.writerId) && Objects.equals(contents, comment.contents) && Objects.equals(createDatetime, comment.createDatetime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, postId, writerId, contents, createDatetime);
    }

    @Override
    public String toString() {
        return "Comment{" +
                "id=" + id +
                ", postId='" + postId + '\'' +
                ", writerId='" + writerId + '\'' +
                ", contents='" + contents + '\'' +
                ", createDatetime=" + createDatetime +
                '}';
    }

    public static Comment of(ResultSet resultSet) throws SQLException {
        Comment comment = new Comment();
        comment.id = resultSet.getLong("id");
        comment.postId = resultSet.getLong("postId");
        comment.writerId = resultSet.getString("writerId");
        comment.contents = resultSet.getString("contents");
        comment.createDatetime = resultSet.getTimestamp("createDatetime");
        return comment;
    }
}

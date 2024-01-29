package model;

import java.util.Date;
import java.util.Objects;

public class Comment {

    private Long id;
    private Long postId;
    private String writerId;
    private String content;
    private Date createDatetime;

    public Comment(Long id, Long postId, String writerId, String content, Date createDatetime) {
        this.id = id;
        this.postId = postId;
        this.writerId = writerId;
        this.content = content;
        this.createDatetime = createDatetime;
    }

    public Comment(Long postId, String writerId, String content, Date createDatetime) {
        this.postId = postId;
        this.writerId = writerId;
        this.content = content;
        this.createDatetime = createDatetime;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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
        return Objects.equals(id, comment.id) && Objects.equals(postId, comment.postId) && Objects.equals(writerId, comment.writerId) && Objects.equals(content, comment.content) && Objects.equals(createDatetime, comment.createDatetime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, postId, writerId, content, createDatetime);
    }

    @Override
    public String toString() {
        return "Comment{" +
                "id=" + id +
                ", postId='" + postId + '\'' +
                ", writerId='" + writerId + '\'' +
                ", content='" + content + '\'' +
                ", createDatetime=" + createDatetime +
                '}';
    }
}

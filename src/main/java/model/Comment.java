package model;

import java.util.Date;
import java.util.Objects;

public class Comment {

    private Long id;
    private Long qnaId;
    private String writerId;
    private String content;
    private Date createDatetime;

    public Comment(Long id, Long qnaId, String writerId, String content, Date createDatetime) {
        this.id = id;
        this.qnaId = qnaId;
        this.writerId = writerId;
        this.content = content;
        this.createDatetime = createDatetime;
    }

    public Comment(Long qnaId, String writerId, String content, Date createDatetime) {
        this.qnaId = qnaId;
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

    public Long getQnaId() {
        return qnaId;
    }

    public void setQnaId(Long qnaId) {
        this.qnaId = qnaId;
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
        return Objects.equals(id, comment.id) && Objects.equals(qnaId, comment.qnaId) && Objects.equals(writerId, comment.writerId) && Objects.equals(content, comment.content) && Objects.equals(createDatetime, comment.createDatetime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, qnaId, writerId, content, createDatetime);
    }

    @Override
    public String toString() {
        return "Comment{" +
                "id=" + id +
                ", qnaId='" + qnaId + '\'' +
                ", writerId='" + writerId + '\'' +
                ", content='" + content + '\'' +
                ", createDatetime=" + createDatetime +
                '}';
    }
}

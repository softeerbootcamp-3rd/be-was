package model;

import java.util.Date;
import java.util.Objects;

public class Qna {

    private Long id;
    private String writerId;
    private String title;
    private String content;
    private Date createDatetime;

    public Qna() {}

    public Qna(Long id, String writerId, String title, String content, Date createDatetime) {
        this.id = id;
        this.writerId = writerId;
        this.title = title;
        this.content = content;
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

    public String getContent() {
        return content;
    }

    public Date getCreateDatetime() {
        return createDatetime;
    }

    @Override
    public String toString() {
        return "Qna{" +
                "id=" + id +
                ", writerId='" + writerId + '\'' +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", createDatetime=" + createDatetime +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Qna qna = (Qna) o;
        return Objects.equals(id, qna.id) && Objects.equals(writerId, qna.writerId) && Objects.equals(title, qna.title)
                && Objects.equals(content, qna.content) && Objects.equals(createDatetime, qna.createDatetime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, writerId, title, content, createDatetime);
    }
}

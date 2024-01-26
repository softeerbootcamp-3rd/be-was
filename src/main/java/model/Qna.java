package model;

import java.util.Date;
import java.util.Objects;

public class Qna {

    private Long id;
    private String writerId;
    private String title;
    private String contents;
    private Date createDatetime;

    public Qna() {}

    public Qna(Long id, String writerId, String title, String contents, Date createDatetime) {
        this.id = id;
        this.writerId = writerId;
        this.title = title;
        this.contents = contents;
        this.createDatetime = createDatetime;
    }

    public Qna(String writerId, String title, String contents, Date createDatetime) {
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
        return "Qna{" +
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
        Qna qna = (Qna) o;
        return Objects.equals(id, qna.id) && Objects.equals(writerId, qna.writerId) && Objects.equals(title, qna.title)
                && Objects.equals(contents, qna.contents) && Objects.equals(createDatetime, qna.createDatetime);
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
}

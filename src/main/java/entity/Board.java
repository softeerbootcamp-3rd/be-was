package entity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.Objects;

public class Board {

    private Long id;
    private String writerId;
    private String title;
    private String contents;
    private Date createDatetime;

    public Board() {}

    public Board(Long id, String writerId, String title, String contents, Date createDatetime) {
        this.id = id;
        this.writerId = writerId;
        this.title = title;
        this.contents = contents;
        this.createDatetime = createDatetime;
    }

    public Board(String writerId, String title, String contents, Date createDatetime) {
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
        return "Board{" +
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
        Board board = (Board) o;
        return Objects.equals(id, board.id) && Objects.equals(writerId, board.writerId) && Objects.equals(title, board.title)
                && Objects.equals(contents, board.contents) && Objects.equals(createDatetime, board.createDatetime);
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

    public static Board of(ResultSet resultSet) throws SQLException {
        Board board = new Board();
        board.id = resultSet.getLong("id");
        board.writerId = resultSet.getString("writerId");
        board.title = resultSet.getString("title");
        board.contents = resultSet.getString("contents");
        board.createDatetime = resultSet.getTimestamp("createDatetime");
        return board;
    }
}

package entity;

import java.sql.Blob;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Objects;

public class Attachment {

    private Long id;
    private Long boardId;
    private String filename;
    private String mimeType;
    private String savedPath;

    public Attachment(Long boardId, String filename, String mimeType, String savedPath) {
        this.boardId = boardId;
        this.filename = filename;
        this.mimeType = mimeType;
        this.savedPath = savedPath;
    }

    public Attachment() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getBoardId() {
        return boardId;
    }

    public void setBoardId(Long boardId) {
        this.boardId = boardId;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    @Override
    public String toString() {
        return "Attachment{" +
                "id=" + id +
                ", boardId=" + boardId +
                ", fileName='" + filename + '\'' +
                ", mimeType='" + mimeType + '\'' +
                '}';
    }

    public static Attachment of(ResultSet resultSet) throws SQLException {
        Attachment attachment = new Attachment();
        attachment.id = resultSet.getLong("id");
        attachment.boardId = resultSet.getLong("boardId");
        attachment.filename = resultSet.getString("filename");
        attachment.mimeType = resultSet.getString("mimeType");
        attachment.savedPath = resultSet.getString("savedPath");
        return attachment;
    }

    public String getSavedPath() {
        return savedPath;
    }

    public void setSavedPath(String savedPath) {
        this.savedPath = savedPath;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Attachment that = (Attachment) o;
        return Objects.equals(id, that.id) && Objects.equals(boardId, that.boardId) && Objects.equals(filename, that.filename) && Objects.equals(mimeType, that.mimeType) && Objects.equals(savedPath, that.savedPath);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, boardId, filename, mimeType, savedPath);
    }
}

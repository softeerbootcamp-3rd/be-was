package model;

import java.sql.Blob;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Objects;

public class Attachment {

    private Long id;
    private Long postId;
    private String filename;
    private String mimeType;
    private byte[] data;

    public Attachment(Long postId, String filename, String mimeType, byte[] data) {
        this.postId = postId;
        this.filename = filename;
        this.mimeType = mimeType;
        this.data = data;
    }

    public Attachment() {
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

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
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
                ", postId=" + postId +
                ", fileName='" + filename + '\'' +
                ", mimeType='" + mimeType + '\'' +
                ", data=" + Arrays.toString(data) +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Attachment attachment = (Attachment) o;
        return Objects.equals(id, attachment.id) && Objects.equals(postId, attachment.postId) && Objects.equals(filename, attachment.filename) && Objects.equals(mimeType, attachment.mimeType) && Arrays.equals(data, attachment.data);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(id, postId, filename, mimeType);
        result = 31 * result + Arrays.hashCode(data);
        return result;
    }

    public static Attachment of(ResultSet resultSet) throws SQLException {
        Attachment attachment = new Attachment();
        attachment.id = resultSet.getLong("id");
        attachment.postId = resultSet.getLong("postId");
        attachment.filename = resultSet.getString("filename");
        attachment.mimeType = resultSet.getString("mimeType");
        Blob blob = resultSet.getBlob("data");
        if (blob != null)
            attachment.data = blob.getBytes(1, (int) blob.length());
        return attachment;
    }
}

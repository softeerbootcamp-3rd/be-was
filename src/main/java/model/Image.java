package model;

import java.sql.Blob;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Date;
import java.util.Objects;

public class Image {

    private Long id;
    private Long postId;
    private String extension;
    private byte[] data;

    public Image(Long postId, String extension, byte[] data) {
        this.postId = postId;
        this.extension = extension;
        this.data = data;
    }

    public Image() {
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

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Image image = (Image) o;
        return Objects.equals(id, image.id) && Objects.equals(postId, image.postId) && Objects.equals(extension, image.extension) && Arrays.equals(data, image.data);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(id, postId, extension);
        result = 31 * result + Arrays.hashCode(data);
        return result;
    }

    @Override
    public String toString() {
        return "Image{" +
                "id=" + id +
                ", postId='" + postId + '\'' +
                ", extension='" + extension + '\'' +
                ", data=" + Arrays.toString(data) +
                '}';
    }

    public static Image of(ResultSet resultSet) throws SQLException {
        Image image = new Image();
        image.id = resultSet.getLong("id");
        image.postId = resultSet.getLong("postId");
        image.extension = resultSet.getString("extension");
        Blob blob = resultSet.getBlob("data");
        if (blob != null)
            image.data = blob.getBytes(1, (int) blob.length());
        return image;
    }
}

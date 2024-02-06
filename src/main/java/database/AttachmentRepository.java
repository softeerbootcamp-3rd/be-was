package database;

import model.Attachment;

import java.sql.*;

public class AttachmentRepository {

    public static void add(Attachment attachment) {
        String query = "INSERT INTO attachments (postId, filename, mimeType, data) VALUES (?, ?, ?, ?)";
        try (Connection connection = H2Database.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, attachment.getPostId());
            statement.setString(2, attachment.getFilename());
            statement.setString(3, attachment.getMimeType());

            Blob blob = connection.createBlob();
            blob.setBytes(1, attachment.getData());
            statement.setBlob(4, blob);

            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public static Attachment findByPostId(Long postId) {
        String query = "SELECT * FROM attachments WHERE postId = ?";
        try (PreparedStatement statement = H2Database.getConnection().prepareStatement(query)) {
            statement.setLong(1, postId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Attachment.of(resultSet);
                } else {
                    return null;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void deleteByPostId(Long postId) {
        String query = "DELETE FROM attachments WHERE postId = ?";
        try (PreparedStatement statement = H2Database.getConnection().prepareStatement(query)) {
            statement.setLong(1, postId);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

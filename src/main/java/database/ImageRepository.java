package database;

import model.Image;
import model.User;

import java.sql.*;

public class ImageRepository {

    public static void add(Image image) {
        String query = "INSERT INTO images (postId, extension, data) VALUES (?, ?, ?)";
        try (Connection connection = H2Database.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, image.getPostId());
            statement.setString(2, image.getExtension());

            Blob blob = connection.createBlob();
            blob.setBytes(1, image.getData());
            statement.setBlob(3, blob);

            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public static Image findByPostId(Long postId) {
        String query = "SELECT * FROM images WHERE postId = ?";
        try (PreparedStatement statement = H2Database.getConnection().prepareStatement(query)) {
            statement.setLong(1, postId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Image.of(resultSet);
                } else {
                    return null;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void deleteByPostId(Long postId) {
        String query = "DELETE FROM images WHERE postId = ?";
        try (PreparedStatement statement = H2Database.getConnection().prepareStatement(query)) {
            statement.setLong(1, postId);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

package database;

import entity.Attachment;

import java.sql.*;

public class AttachmentRepository {

    public static void add(Attachment attachment) {
        String query = "insert: attachments: boardId, filename, mimeType, savedPath: ?, ?, ?, ?";
        try (Connection connection = CsvDatabase.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, attachment.getBoardId());
            statement.setString(2, attachment.getFilename());
            statement.setString(3, attachment.getMimeType());
            statement.setString(4, attachment.getSavedPath());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public static Attachment findByBoardId(Long boardId) {
        String query = "select: attachments: boardId: ?";
        try (PreparedStatement statement = CsvDatabase.getConnection().prepareStatement(query)) {
            statement.setLong(1, boardId);
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

    public static void deleteByBoardId(Long boardId) {
        String query = "delete: attachments: boardId: ?";
        try (PreparedStatement statement = CsvDatabase.getConnection().prepareStatement(query)) {
            statement.setLong(1, boardId);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

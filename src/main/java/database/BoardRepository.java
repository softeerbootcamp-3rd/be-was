package database;

import entity.Board;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.*;

public class BoardRepository {

    public static Long add(Board board) {
        String query = "INSERT INTO boards (writerId, title, contents, createDatetime) VALUES (?, ?, ?, ?)";
        try (PreparedStatement statement = H2Database.getConnection().prepareStatement(query)) {
            statement.setString(1, board.getWriterId());
            statement.setString(2, board.getTitle());
            statement.setString(3, board.getContents());
            statement.setTimestamp(4, new Timestamp(board.getCreateDatetime().getTime()));
            statement.executeUpdate();

            // 생성된 ID 값 얻기
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getLong(1);
                } else {
                    throw new SQLException("Failed to get generated key, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static Board findById(Long id) {
        String query = "SELECT * FROM boards WHERE id = ?";
        try (PreparedStatement statement = H2Database.getConnection().prepareStatement(query)) {
            statement.setLong(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Board.of(resultSet);
                } else {
                    return null;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void deleteById(Long id) {
        String query = "DELETE FROM boards WHERE id = ?";
        try (PreparedStatement statement = H2Database.getConnection().prepareStatement(query)) {
            statement.setLong(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static int countAll() {
        String query = "SELECT COUNT(*) FROM users";
        try (PreparedStatement statement = H2Database.getConnection().prepareStatement(query)) {
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt(1);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return 0;
    }

    public static Collection<Board> getPage(int pageSize, int pageNumber) {
        String query = "SELECT * FROM boards ORDER BY id DESC LIMIT ? OFFSET ?";
        List<Board> boards = new ArrayList<>();

        try (PreparedStatement statement = H2Database.getConnection().prepareStatement(query)) {
            int offset = (pageNumber - 1) * pageSize;

            statement.setInt(1, pageSize);
            statement.setInt(2, offset);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    boards.add(Board.of(resultSet));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return boards;
    }
}

package database;

import entity.Board;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.*;

public class BoardRepository {

    public static Long add(Board board) {
        String query = "insert: boards: writerId, title, contents, createDatetime: ?, ?, ?, ?";
        try (PreparedStatement statement = CsvDatabase.getConnection().prepareStatement(query)) {
            statement.setString(1, board.getWriterId());
            statement.setString(2, board.getTitle());
            statement.setString(3, board.getContents());
            statement.setTimestamp(4, new Timestamp(board.getCreateDatetime().getTime()));
            statement.executeUpdate();

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
        String query = "select: boards: id: ?";
        try (PreparedStatement statement = CsvDatabase.getConnection().prepareStatement(query)) {
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
        String query = "delete: boards: id: ?";
        try (PreparedStatement statement = CsvDatabase.getConnection().prepareStatement(query)) {
            statement.setLong(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static int countAll() {
        String query = "count: boards";
        try (PreparedStatement statement = CsvDatabase.getConnection().prepareStatement(query)) {
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
        String query = "select: boards";
        List<Board> boards = new ArrayList<>();

        try (PreparedStatement statement = CsvDatabase.getConnection().prepareStatement(query)) {
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

        Collections.reverse(boards);
        int startIndex = (pageNumber - 1) * pageSize;
        int endIndex = Math.min(startIndex + pageSize, boards.size());
        return boards.subList(startIndex, endIndex);
    }
}

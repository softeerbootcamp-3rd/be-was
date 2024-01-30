package dao;

import dto.PostDto;
import file.MultipartFile;
import model.File;
import model.Post;
import util.JdbcUtil;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class FileDao {

    private static final Connection connection = JdbcUtil.getJdbcConnection();

    public static void insertFile(File file) {
        String query = "INSERT INTO filestorage (post_id, file_data) VALUES (?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setLong(1, file.getPostId());
            preparedStatement.setBytes(2, file.getFileContent());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static byte[] findFileByPostId(int postId) {
        String query = "SELECT file_data FROM filestorage WHERE post_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, postId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    byte[] content = resultSet.getBytes("file_data");
                    return content;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }


}

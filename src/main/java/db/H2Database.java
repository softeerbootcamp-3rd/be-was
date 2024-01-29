package db;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;

public class H2Database {
    private static final Logger logger = LoggerFactory.getLogger(H2Database.class);
    public static final String url = "jdbc:h2:tcp://localhost:9092/~/was";
    public static final String username = "sa";
    public static final String password = "";

    H2Database() {
    }

    public static void initTable(){
        createUserTable();
        createPostTable();
    }

    private static void createUserTable() {
        String sql = "CREATE TABLE IF NOT EXISTS User (" +
                "id INT PRIMARY KEY AUTO_INCREMENT," +
                "userId VARCHAR(255) NOT NULL," +
                "password VARCHAR(255) NOT NULL," +
                "name VARCHAR(255) NOT NULL," +
                "email VARCHAR(255) NOT NULL" +
                ");";

        try (Connection conn = DriverManager.getConnection(url, username, password); Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }
    }
    private static void createPostTable() {
        String sql = "CREATE TABLE IF NOT EXISTS Post (" +
                "id INT PRIMARY KEY AUTO_INCREMENT," +
                "writer VARCHAR(255) NOT NULL," +
                "title VARCHAR(255) NOT NULL," +
                "content VARCHAR(255) NOT NULL" +
                ");";

        try (Connection conn = DriverManager.getConnection(url, username, password); Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }
    }
}

package db;

import model.Board;
import model.User;
import org.h2.tools.Server;

import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Database {

    private static Connection connection;

    static { // 정적 초기화 블록을 사용하여 클래스가 로딩될 때 데이터베이스 초기화
        String url = "jdbc:h2:mem:webserver"; //h2 in-memory database
        String user = "sa";
        String password = "";

        try {
            connection = DriverManager.getConnection(url, user, password);
            Statement statement = connection.createStatement();
            Server.createWebServer("-web", "-webAllowOthers", "-webPort", "8082").start();

            // USERS 테이블 생성
            String createUsersTable =
                    "CREATE TABLE IF NOT EXISTS USERS (" +
                            "ID INTEGER AUTO_INCREMENT PRIMARY KEY, " +
                            "USER_ID VARCHAR(30) NOT NULL, " +
                            "PASSWORD VARCHAR(30) NOT NULL, " +
                            "NAME VARCHAR(30) NOT NULL, " +
                            "EMAIL VARCHAR(30) NOT NULL, " +
                            "CREATED_AT DATETIME);";
            statement.execute(createUsersTable);

            // BOARD 테이블 생성
            String createBoardTable =
                    "CREATE TABLE IF NOT EXISTS BOARD (" +
                            "ID INTEGER AUTO_INCREMENT PRIMARY KEY, " +
                            "WRITER VARCHAR(30) NOT NULL, " +
                            "TITLE VARCHAR(30) NOT NULL, " +
                            "CONTENT CLOB NOT NULL, " +
                            "CREATED_AT TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                            "FILE_PATH VARCHAR(255));";
            statement.execute(createBoardTable);

            // Dummy Data
            String userQuery = "INSERT INTO USERS (USER_ID, PASSWORD, NAME, EMAIL, CREATED_AT) " +
                    "VALUES ('hyungmin1998', '1234', 'hyungmin', 'test@test.com', '1998-12-31')";
            statement.execute(userQuery);

            String boardQuery = "INSERT INTO BOARD (WRITER, TITLE, CONTENT, CREATED_AT, FILE_PATH) " +
                    "VALUES ('hyungmin', 'I am a good person!', 'I am hyungmin! I like talking and communicating with other people!', '2023-01-29', '/1.jpg')";
            statement.execute(boardQuery);

            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to initialize database", e);
        }
    }

    public static void addUser(User user) { // 사용자 추가
        String insertQuery = "INSERT INTO USERS (USER_ID, PASSWORD, NAME, EMAIL, CREATED_AT) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
            preparedStatement.setString(1, user.getUserId());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.setString(3, user.getName());
            preparedStatement.setString(4, user.getEmail());
            preparedStatement.setString(5, LocalDate.now().format(DateTimeFormatter.ISO_DATE));

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static User findUserById(String userId) { // 사용자 조회
        String query = "SELECT * FROM USERS WHERE USER_ID = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return new User(
                        resultSet.getInt("ID"),
                        resultSet.getString("USER_ID"),
                        resultSet.getString("PASSWORD"),
                        resultSet.getString("NAME"),
                        resultSet.getString("EMAIL"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static List<User> findAllUsers() { // 모든 사용자 조회
        String query = "SELECT * FROM USERS";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            List<User> users = new ArrayList<>();
            while (resultSet.next()) {
                users.add(new User(
                        resultSet.getInt("ID"),
                        resultSet.getString("USER_ID"),
                        resultSet.getString("PASSWORD"),
                        resultSet.getString("NAME"),
                        resultSet.getString("EMAIL")));
            }
            return users;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void addBoard(Board board) { // 게시글 추가
        String query = "INSERT INTO BOARD (WRITER, TITLE, CONTENT, CREATED_AT, FILE_PATH) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, board.getWriter());
            preparedStatement.setString(2, board.getTitle());
            preparedStatement.setString(3, board.getContent());
            preparedStatement.setString(4, LocalDate.now().format(DateTimeFormatter.ISO_DATE));
            preparedStatement.setString(5, board.getFileUpload());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static Board findBoardById(Integer id) { // 게시글 조회
        String query = "SELECT * FROM BOARD WHERE ID = " + id;
        try (PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            if (resultSet.next()) {
                return new Board(
                        resultSet.getInt("ID"),
                        resultSet.getString("WRITER"),
                        resultSet.getString("TITLE"),
                        resultSet.getString("CONTENT"),
                        resultSet.getDate("CREATED_AT").toLocalDate(),
                        resultSet.getString("FILE_PATH"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

        public static List<Board> findAllBoard() { // 모든 게시글 조회
        String query = "SELECT * FROM BOARD";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            List<Board> boards = new ArrayList<>();
            while (resultSet.next()) {
                boards.add(new Board(
                        resultSet.getInt("ID"),
                        resultSet.getString("WRITER"),
                        resultSet.getString("TITLE"),
                        resultSet.getString("CONTENT"),
                        resultSet.getDate("CREATED_AT").toLocalDate(),
                        resultSet.getString("FILE_PATH")));
            }
            return boards;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

package controller;

import httpmessage.request.HttpRequest;
import httpmessage.response.HttpResponse;
import model.Article;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;

public class WritingController implements Controller {
    public  void service(HttpRequest httpRequest, HttpResponse httpResponse) throws ClassNotFoundException {

        Class.forName("org.h2.Driver");
        String url = "jdbc:h2:~/wasDB";
        String user = "sa";
        String password = "";

        // Connect to the database
        try (Connection connection = DriverManager.getConnection(url, user, password)) {

            createTable(connection);

            Article article = new Article(httpRequest);
            insertArticle(connection, article);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static void createTable(Connection connection) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(
                "CREATE TABLE IF NOT EXISTS Article " +
                        "(id INT AUTO_INCREMENT PRIMARY KEY, title VARCHAR(255), " +
                        "userid VARCHAR(255), content VARCHAR(255),createdate VARCHAR(255)) ")) {
            preparedStatement.execute();
        }catch (SQLException e) {
            System.out.println(e);
        }
    }

    private static void insertArticle(Connection connection, Article article) {

        try (PreparedStatement preparedStatement = connection.prepareStatement(
                "INSERT INTO Article (title,userid, content,createdate) " +
                        "VALUES (?,?,?,?)")) {
            preparedStatement.setString(1, article.getTitle());
            preparedStatement.setString(2, article.getUserId());
            preparedStatement.setString(3, article.getContents());
            preparedStatement.setString(4, article.getCreatedate());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e);
        }
    }
}



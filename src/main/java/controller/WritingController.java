package controller;

import httpmessage.HttpStatusCode;
import httpmessage.request.HttpRequest;
import httpmessage.response.HttpResponse;
import model.Article;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Objects;

public class WritingController implements Controller {
    public  void service(HttpRequest httpRequest, HttpResponse httpResponse) throws ClassNotFoundException {

        if(Objects.equals(httpRequest.getHttpMethod(), "GET")){
            httpResponse.setHttpStatusCode(HttpStatusCode.MOVED_TEMPORARILY);
            if(!httpRequest.getCookie().isEmpty()){
                httpResponse.setRedirectionPath("/qna/form.html");
            }
            else{
                httpResponse.setRedirectionPath("/user/login.html");
            }
            return;
        }

        Class.forName("org.h2.Driver");
        String url = "jdbc:h2:~/wasDB";
        String user = "sa";
        String password = "";

        // Connect to the database
        try (Connection connection = DriverManager.getConnection(url, user, password)) {

            //dropTable(connection);
            createTable(connection);
            Article article = new Article(httpRequest);
            insertArticle(connection, article);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        httpResponse.setHttpStatusCode(HttpStatusCode.MOVED_TEMPORARILY);
        httpResponse.setRedirectionPath("/index.html");
    }

    private static void dropTable(Connection connection) {
        try (PreparedStatement dropStatement = connection.prepareStatement("DROP TABLE IF EXISTS Article")) {
            dropStatement.execute();
        } catch (SQLException e) {
        }
    }

    private static void createTable(Connection connection) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(
                "CREATE TABLE IF NOT EXISTS Article " +
                        "(id INT AUTO_INCREMENT PRIMARY KEY, title VARCHAR(255), " +
                        "userid VARCHAR(255), content VARCHAR(255),createdate VARCHAR(255)) ")) {
            preparedStatement.execute();
        }catch (SQLException e) {
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



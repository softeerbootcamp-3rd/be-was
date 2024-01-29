package controller;

import db.Database;
import httpmessage.request.HttpRequest;
import httpmessage.response.HttpResponse;
import model.Article;
import model.Content;
import model.User;
import org.checkerframework.framework.qual.LiteralKind;

import java.sql.*;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class HtmlController implements Controller{
    public void service(HttpRequest httpRequest, HttpResponse httpResponse) throws ClassNotFoundException {
        final String requestPath =  httpRequest.getPath();
        String body = new String(httpResponse.getBody());

        Content loginContent = new Content(httpRequest,httpResponse);
        String topContent = loginContent.getString();
        String modifiedContent = body.replace("{{login}}",topContent);

        if(requestPath.contains("list")){
            Database database = new Database();
            Collection<User> users = database.findAll();
            Content listContent = new Content(users);

            String userConent = listContent.getString();
            modifiedContent = modifiedContent.replace("{{list}}",userConent);
        }

        if(requestPath.contains("index")) {
            Class.forName("org.h2.Driver");
            String url = "jdbc:h2:~/wasDB";
            String user = "sa";
            String password = "";

            try (Connection connection = DriverManager.getConnection(url, user, password)) {

                LinkedList<Article> articles = fetchArticles(connection);
                Content articlesContent = new Content(articles);

                String ArticleList = articlesContent.getString();
                modifiedContent = modifiedContent.replace("{{articleList}}",ArticleList);

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        if(requestPath.contains("show")){
            Class.forName("org.h2.Driver");
            String url = "jdbc:h2:~/wasDB";
            String user = "sa";
            String password = "";
            String articleId = httpRequest.getPath().split("=")[1];

            try (Connection connection = DriverManager.getConnection(url, user, password)) {
                Article article = specificArticle(connection,articleId);
                System.out.println(article);

                modifiedContent = new Content().detailContent(article,modifiedContent);

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }



        byte[] modifiedBody = modifiedContent.getBytes();
        httpResponse.setBody(modifiedBody);
    }

    private Article specificArticle(Connection connection, String articleId) throws SQLException {
        Article article = new Article();

        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Article WHERE id = "+articleId)) {
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                article.setTitle(resultSet.getString("title"));
                article.setUserId(resultSet.getString("userid"));
                article.setContents(resultSet.getString("content"));
                article.setCreatedate(resultSet.getString("createdate"));
            }

            return article;
        }
    }

    private LinkedList<Article> fetchArticles(Connection connection) throws SQLException {
        LinkedList<Article> articles = new LinkedList<>();

        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Article")) {
            ResultSet resultSet = preparedStatement.executeQuery();

            if (!resultSet.isBeforeFirst()) {
                return articles;
            }

            while (resultSet.next()) {
                Article article = new Article();

                article.setArticleId(Long.valueOf(resultSet.getString("id")));
                article.setTitle(resultSet.getString("title"));
                article.setUserId(resultSet.getString("userid"));
                article.setContents(resultSet.getString("content"));
                article.setCreatedate(resultSet.getString("createdate"));

                articles.add(article);
            }
            return articles;
        }
    }
}


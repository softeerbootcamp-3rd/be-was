//test
package db;


import model.User;
import model.Post;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.WebServer;
import utils.FileUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class Database {
    private static final Logger logger = LoggerFactory.getLogger(WebServer.class);
    private static ConcurrentHashMap<String, User> users = new ConcurrentHashMap<>();
    private static List<Post> posts = new ArrayList<>();
    private static String homePageHtml; // 홈페이지 HTML 내용을 저장하는 변수

    static {
        try {
            homePageHtml = FileUtils.readFileAsString("templates/index.html");
        } catch (IOException e) {
            logger.error("Error loading initial home page HTML: {}", e.getMessage(), e);
            homePageHtml = "Error loading page";
        }
    }
    public static void addUser(User user) {
        users.put(user.getUserId(), user);
    }

    public static User findUserById(String userId) {
        return users.get(userId);
    }

    public static List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    public static void addPost(Post post) {
        posts.add(post);
        // 홈페이지 HTML 업데이트는 별도의 서비스나 컨트롤러에서 처리
    }

    public static String getHomePageHtml() {
        return homePageHtml;
    }

    public static void setHomePageHtml(String html) {
        homePageHtml = html;
    }

    public static List<Post> getAllPosts() {
        return posts;
    }
}
